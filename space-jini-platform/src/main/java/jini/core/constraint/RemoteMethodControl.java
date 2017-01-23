package jini.core.constraint;

/**
 * Created by zksun on 23/01/2017.
 */
public interface RemoteMethodControl {
    RemoteMethodControl setConstraints(MethodConstraints constraints);

    MethodConstraints getConstraints();
}
