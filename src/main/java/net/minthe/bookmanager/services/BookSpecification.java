package net.minthe.bookmanager.services;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import net.minthe.bookmanager.models.Book;
import org.springframework.data.jpa.domain.Specification;

/** Created by Michael Kelley on 9/13/2020 */
public class BookSpecification implements Specification<Book> {
  private final BookFilter filter;

  public BookSpecification(BookFilter filter) {
    this.filter = filter;
  }

  @Override
  public Predicate toPredicate(
      Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    var p = criteriaBuilder.disjunction();
    if (filter.getSearch() != null && !filter.getSearch().equals("")) {
      p.getExpressions()
          .add(criteriaBuilder.like(root.get("title"), "%" + filter.getSearch() + "%"));
    }
    if (filter.getUnfinished() != null && filter.getUnfinished()) {
      p.getExpressions().add(root.join("bookReads").get("finished").isNull());
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
