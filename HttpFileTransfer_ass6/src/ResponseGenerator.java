public class ResponseGenerator {
    private String httpVersion;
    private String httpOK;
    private String httpNotFound;

    public ResponseGenerator(){
        httpVersion = "HTTP/1.1 ";
        httpOK = "200 OK\n";
        httpNotFound = "404 NotFound\n";
    }


    public String positiveResponse(String contentType, long length){
        String pos = httpVersion + httpOK;
        pos = pos + "Content-length: " + length + "\n";
        pos = pos + "Content-Type:" + contentType + "\n\n";
        return pos;
    }

    public String negativeResponse(){
        String neg = httpVersion + httpNotFound + "Content-Type: text/html\n\n";
        return neg;
    }

}
