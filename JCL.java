import com.nativelibs4java.opencl.*;
import com.nativelibs4java.util.*;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.bridj.Pointer;

public class JCL {
	//final CLQueue queue;
    final CLContext context;
    final CLProgram program;
    final CLKernel kernelM;
    CLEvent multiplyEvent;
    public JCL() throws IOException, CLBuildException {    	
    	this.context = JavaCL.createBestContext();   
    	//this.queue = context.createDefaultOutOfOrderQueueIfPossible();    	
        String source = IOUtils.readText(JCL.class.getResource("test.cl"));
        program = context.createProgram(source);
        kernelM = program.createKernel("multiply");
    }
    public synchronized Pointer<Float> multiply(FloatBuffer in1, FloatBuffer in2) {
    	CLQueue queue = context.createDefaultOutOfOrderQueueIfPossible(); 
        int length = in1.capacity();
        Pointer<Float> out;
        CLBuffer<Float> inBuf1 = context.createFloatBuffer(CLMem.Usage.Input, in1, true); // true = copy
        CLBuffer<Float> inBuf2 = context.createFloatBuffer(CLMem.Usage.Input, in2, true); // true = copy
        
        // Create an output CLBuffer :
        CLBuffer<Float> outBuf = context.createFloatBuffer(CLMem.Usage.Output, length);

        // Set the args of the kernel :
        kernelM.setArgs(inBuf1,inBuf2, outBuf, length);
        multiplyEvent = kernelM.enqueueNDRange(queue, new int[]{length});
        // Ask for `length` parallel executions of the kernel in 1 dimension :
        
        out = outBuf.read(queue, multiplyEvent);
        // Return an pointer read from the output CLBuffer :
        inBuf1.release();
        inBuf2.release();
        outBuf.release();
        queue.release();
        return out;
    }

    /// Wrapper method that takes and returns double arrays
    public float[] multiply(float[] in1, float[] in2) {    	
        Pointer<Float> outBuffer = multiply(FloatBuffer.wrap(in1), FloatBuffer.wrap(in2));
        float[] out = new float[in1.length];        
        return (float[]) outBuffer.getArray();
    }
}
