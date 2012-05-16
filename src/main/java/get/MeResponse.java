package get;
import org.codehaus.jackson.annotate.JsonProperty;

public class MeResponse extends AbstractResource {

  public static class Apps extends AbstractResource {

    public static class App extends AbstractResource {
      private int id;
      private String title;
      private String role;

      public String getTitle() {
        return title;
      }

      public void setTitle(String title) {
        this.title = title;
      }

      public String getRole() {
        return role;
      }

      public void setRole(String role) {
        this.role = role;
      }

      public int getId() {
        return id;
      }

      public void setId(int id) {
        this.id = id;
      }

    }

    private int id;
    private App[] all;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public App[] getAll() {
      return all;
    }

    public void setAll(App[] all) {
      this.all = all;
    }

  }

  public static class Keys extends AbstractResource {
    public static class Platform extends AbstractResource {
      public static class Key extends AbstractResource {
        private int id;
        @JsonProperty("default")
        private boolean defaultKey;
        private String title;

        public int getId() {
          return id;
        }

        public void setId(int id) {
          this.id = id;
        }

        public boolean isDefault() {
          return defaultKey;
        }

        public void setDefault(boolean defaultKey) {
          this.defaultKey = defaultKey;
        }

        public String getTitle() {
          return title;
        }

        public void setTitle(String title) {
          this.title = title;
        }

      }

      private Key[] all;

      public Key[] getAll() {
        return all;
      }

      public void setAll(Key[] all) {
        this.all = all;
      }

    }

    private Platform ios;
    private Platform blackberry;
    private Platform android;

    public Platform getIos() {
      return ios;
    }

    public void setIos(Platform ios) {
      this.ios = ios;
    }

    public Platform getBlackberry() {
      return blackberry;
    }

    public void setBlackberry(Platform blackberry) {
      this.blackberry = blackberry;
    }

    public Platform getAndroid() {
      return android;
    }

    public void setAndroid(Platform android) {
      this.android = android;
    }

  }

  private int id;
  private String username;
  private String email;
  private Apps apps;
  private Keys keys;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Apps getApps() {
    return apps;
  }

  public void setApps(Apps apps) {
    this.apps = apps;
  }

  public Keys getKeys() {
    return keys;
  }

  public void setKeys(Keys keys) {
    this.keys = keys;
  }

}
