import io.toolisticon.spiap.api.SpiService;
import io.toolisticon.spiap.processor.serviceprocessortest.TestSpiInterface;

@SpiService(TestSpiInterface.class)
public class TestcaseValidUseWithPlainInterface implements TestSpiInterface {
    public String doSomething() {
        return "";
    }
}