package chap12;

import java.util.Objects;

public record NameHolderWithExtras(String firstName, String lastName) {

    // Records can have statics
    private static int instanceCount = 0;

    // Records cannot define non-static types other than those defined in record (e.g. firstName, lastName)
    // This would not compile
    //private int variable = 0;

    public NameHolderWithExtras {
        ++instanceCount;
    }

    /**
     * You can overload constructors but the default is always the one that corresponds to the record definition
     * (e.g. NameHolderWithExtras(String String))
     */
    public NameHolderWithExtras() {
        this(null, null);
    }

    /**
     * Static methods are permitted.
     */
    public static int getInstanceCount() {
        return instanceCount;
    }

    /**
     * Methods can be overridden
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameHolderWithExtras that = (NameHolderWithExtras) o;
        return Objects.equals(lastName, that.lastName);
    }

    /**
     * Methods can be overridden
     */
    @Override
    public int hashCode() {
        return Objects.hash(lastName);
    }

    /**
     * Methods can be overridden
     */
    @Override
    public String toString() {
        return "NameHolderWithExtras{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
