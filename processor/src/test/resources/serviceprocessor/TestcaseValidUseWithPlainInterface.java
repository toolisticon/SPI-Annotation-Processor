import io.toolisticon.spiap.api.Service;
import io.toolisticon.spiap.processor.serviceprocessortest.TestSpiInterface;

@Service(TestSpiInterface.class)
public class TestcaseValidUseWithPlainInterface implements TestSpiInterface {
    public String doSomething() {
        return "";
    }
}