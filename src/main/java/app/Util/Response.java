package app.Util;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class Response {
    @JsonInclude(NON_NULL)
    private String status;
    @JsonInclude(NON_NULL)
    private Object data;
    @JsonInclude(NON_NULL)
    private String error;

    public enum Status {
        OK,
        ERROR
        ;

        public boolean isOk() {
            return this.name().equals("OK");
        }

        public boolean isError() {
            return this.name().equals("ERROR");
        }
    }

    public Response(Status status, Object data) {
        this.status = status.name();

        if (status.isOk()) {
            this.data = data;
        } else {
            this.error = data.toString();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
