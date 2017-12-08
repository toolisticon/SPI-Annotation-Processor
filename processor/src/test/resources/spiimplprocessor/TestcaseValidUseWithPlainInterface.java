import io.toolisticon.spiap.api.SpiImpl;
import io.toolisticon.spiap.processor.spiimplprocessortest.TestSpiInterface;

@SpiImpl(TestSpiInterface.class)
public class TestcaseValidUseWithPlainInterface implements TestSpiInterface {
    public String doSomething() {
        return "";
    }
}