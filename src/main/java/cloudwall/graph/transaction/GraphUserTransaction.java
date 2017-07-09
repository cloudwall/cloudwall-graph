package cloudwall.graph.transaction;

import javax.transaction.*;

/**
 * A user-managed transaction controlling the commit/rollback point for modifications to a {@link TransactionalGraph}.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class GraphUserTransaction implements UserTransaction {
    private static ThreadLocal<GraphUserTransaction> currentTransaction = ThreadLocal.withInitial(GraphUserTransaction::new);

    @Override
    public void begin() throws NotSupportedException, SystemException {

    }

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {

    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {

    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {

    }

    @Override
    public int getStatus() throws SystemException {
        return Status.STATUS_UNKNOWN;
    }

    @Override
    public void setTransactionTimeout(int seconds) throws SystemException {

    }
}
