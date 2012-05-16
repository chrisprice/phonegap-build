package get;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppResponse extends AbstractResource {
  private String title;
  private int id;
  private String version;
  private AppStatusResponse status;
  private AppDownloadResponse download;
  private String repo;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public AppStatusResponse getStatus() {
    return status;
  }

  public void setStatus(AppStatusResponse status) {
    this.status = status;
  }

  public AppDownloadResponse getDownload() {
    return download;
  }

  public void setDownload(AppDownloadResponse download) {
    this.download = download;
  }

  public String getRepo() {
    return repo;
  }

  public void setRepo(String repo) {
    this.repo = repo;
  }

}