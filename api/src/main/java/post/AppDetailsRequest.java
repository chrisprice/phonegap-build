package post;

import org.codehaus.jackson.annotate.JsonProperty;

public class AppDetailsRequest {
  private String title;
  @JsonProperty("package")
  private String packageName;
  private String version;
  @JsonProperty("create_method")
  private String createMethod;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCreateMethod() {
    return createMethod;
  }

  public void setCreateMethod(String createMethod) {
    this.createMethod = createMethod;
  }

  public String getPackage() {
    return packageName;
  }

  public void setPackage(String packageName) {
    this.packageName = packageName;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

}
