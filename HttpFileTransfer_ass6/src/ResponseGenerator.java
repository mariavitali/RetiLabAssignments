public class ResponseGenerator {
    private String httpVersion;
    private String httpOK;
    private String httpEndResponse;

    public ResponseGenerator(){
        httpVersion = "HTTP/1.1 ";
        httpOK = "200 OK";
        httpEndResponse = "\r\n\r\n";
    }

    public String printMenu(){
        //retrieve menu file
        String instructions = httpVersion + httpOK + httpEndResponse + "How to use the service:\n";
        //System.out.println(instructions);
        return instructions;
    }
}
