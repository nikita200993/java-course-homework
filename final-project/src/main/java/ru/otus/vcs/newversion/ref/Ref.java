package ru.otus.vcs.newversion.ref;

import ru.otus.utils.Contracts;

public abstract class Ref {

    protected final String refString;

    protected Ref(final String refString) {
        Contracts.requireNonNullArgument(refString);

        this.refString = refString;
    }

    public String getRefString() {
        return refString;
    }

    public static Ref create(final String refString) {
        Contracts.requireNonNullArgument(refString);
        Contracts.requireThat(isValidRefString(refString));

        if (Sha1.isValidSha1HexString(refString)) {
            return Sha1.create(refString);
        } else if (BranchName.isValidBranchName(refString)) {
            return BranchName.create(refString);
        } else {
            return ReservedRef.forRefString(refString);
        }
    }

    public static boolean isValidRefString(final String refString) {
        Contracts.requireNonNullArgument(refString);

        return Sha1.isValidSha1HexString(refString)
                || ReservedRef.isValidReservedRefString(refString)
                || BranchName.isValidBranchName(refString);
    }
}
