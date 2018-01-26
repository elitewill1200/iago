__kernel void multiply(__global const float *in1, __global const float *in2, __global float *out, int length)
{
	// Get the varying parameter of the parallel execution :
	int i = get_global_id(0);
	
	// In case we're executed "too much", check bounds :
	if (i >= length)
		return;

	out[i] = in1[i] * in2[i];
}