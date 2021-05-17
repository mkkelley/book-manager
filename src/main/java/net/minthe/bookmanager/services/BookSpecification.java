package net.minthe.bookmanager.services;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import net.minthe.bookmanager.models.Book;
import net.minthe.bookmanager.models.BookRead;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;

/** Created by Michael Kelley on 9/13/2020 */
public class BookSpecification implements Specification<Book> {
  private final BookFilter filter;

  public BookSpecification(BookFilter filter) {
    this.filter = filter;
  }

  @Override
  public Predicate toPredicate(
      Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    var p = criteriaBuilder.conjunction();
    if (filter.getSearch() != null && !filter.getSearch().equals("")) {
      var escapedSearch = filter.getSearch().replace("\\", "\\\\").replace("%", "\\%");
      p.getExpressions()
          .add(
              criteriaBuilder.or(
                  criteriaBuilder.like(
                      criteriaBuilder.upper(root.get("title")),
                      criteriaBuilder.upper(criteriaBuilder.literal("%" + escapedSearch + "%")),
                      '\\'),
                  criteriaBuilder.like(
                      criteriaBuilder.upper(root.get("author").get("name")),
                      criteriaBuilder.upper(criteriaBuilder.literal("%" + escapedSearch + "%")),
                      '\\')));
    }
    if (filter.getUnfinished() != null && filter.getUnfinished()) {
      var bookReadQuery = query.subquery(Book.class);
      var subRoot = bookReadQuery.from(BookRead.class);
      Join<BookRead, Book> bookJoin = subRoot.join("book");

      var values =
          bookReadQuery
              .select(bookJoin)
              .where(
                  criteriaBuilder.and(
                      criteriaBuilder.equal(
                          subRoot.get("username"),
                          SecurityContextHolder.getContext().getAuthentication().getName()),
                      criteriaBuilder.isNull(subRoot.get("finished"))));

      p.getExpressions().add(criteriaBuilder.in(root.get("id")).value(values));
    }
    if (filter.getTag() != null && !filter.getTag().equals("")) {
      p.getExpressions()
          .add(criteriaBuilder.equal(root.join("tags").join("tag").get("tag"), filter.getTag()));
    }

    query.orderBy(criteriaBuilder.desc(root.get("createdAt")));

    if (p.getExpressions().size() == 0) {
      return null;
    }
    return p;
  }
}
