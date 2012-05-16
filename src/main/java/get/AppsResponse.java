package get;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppsResponse extends AbstractResource {
  private AppResponse[] apps;

  public AppResponse[] getApps() {
    return apps;
  }

  public void setApps(AppResponse[] apps) {
    this.apps = apps;
  }

}
