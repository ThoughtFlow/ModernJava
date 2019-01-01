package lab.util;

/**
 * Generic holder class to hold any kinds of object 
 *
 * @param <T>
 * @param <U>
 */
public class BiHolder<T, U> {

	private T t;
	private U u;

	/**
	 * Initializes T and U types.
	 * 
	 * @param t Value to set.
	 * @param u Value to set.
	 */
    public BiHolder(T t, U u)
    {
    	this.t = t;
    	this.u = u;
    }

    /**
     * Returns T
     * @return T
     */
    public T getT() {
    	return t;
    }
    
    /**
     * Returns U.
     *  
     * @return U
     */
    public U getU() {
    	return u;
    }
    
    /**
     * Sets T.
     * @param t Value to set.
     * @return Instance to self.
     */
    public BiHolder<T, U> setT(T t) {
    	this.t = t;
    	return this;
    }
    
    /**
     * Sets U.
     * 
     * @param u Value to set.
     * @return Instance to self.
     */
    public BiHolder<T, U> setU(U u) {
    	this.u = u;
    	return this;
    }
}