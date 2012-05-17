import com.github.chrisprice.phonegapbuild.api.data.ErrorResponse;

@SuppressWarnings("serial")
public class ApiException extends RuntimeException {

  public ApiException(String message) {
    super(message);
  }

  public ApiException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public ApiException(ErrorResponse response, Throwable throwable) {
    super(response.getError(), throwable);
  }

}
