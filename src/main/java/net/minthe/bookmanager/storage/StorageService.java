package net.minthe.bookmanager.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import net.minthe.bookmanager.models.BookStorage;
import net.minthe.bookmanager.repositories.BookStorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Kelley on 9/19/2020
 */
@Service
public class StorageService {

  private final AmazonS3 s3;
  private final AwsS3Settings settings;
  private final BookStorageRepository bookStorageRepository;
  private final Logger logger = LoggerFactory.getLogger(StorageService.class);

  public StorageService(
      AmazonS3 s3, AwsS3Settings settings, BookStorageRepository bookStorageRepository) {
    this.s3 = s3;
    this.settings = settings;
    this.bookStorageRepository = bookStorageRepository;
    if (!s3.doesBucketExistV2(settings.getBucket())) {
      logger.info("Did not find bucket \"{}\". Creating it.", settings.getBucket());
      var createBucketRequest = new CreateBucketRequest(settings.getBucket());
      createBucketRequest.setCannedAcl(CannedAccessControlList.Private);
      s3.createBucket(createBucketRequest);
    }
  }

  public BookStorage uploadFileForBook(Long bookId, String uploadName, InputStream inputStream) {
    var keyPart = UUID.randomUUID().toString();
    var metadata = new ObjectMetadata();
    metadata.addUserMetadata("name", uploadName);
    metadata.setContentType("application/epub+zip");
    var key = bookId + "/" + keyPart;
    var request = new PutObjectRequest(settings.getBucket(), key, inputStream, metadata);
    s3.putObject(request);
    logger.info(
        "Uploading file \"{}\" to \"{}\" in bucket \"{}\"", uploadName, key, settings.getBucket());
    var bookStorage = new BookStorage(null, bookId, null, settings.getBucket(), key, uploadName);
    return bookStorageRepository.save(bookStorage);
  }

  public List<BookStorage> getUploadsForBook(Long bookId) {
    return bookStorageRepository.findByBookId(bookId);
  }
}
