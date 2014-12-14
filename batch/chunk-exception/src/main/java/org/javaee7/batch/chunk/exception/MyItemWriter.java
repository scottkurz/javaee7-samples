package org.javaee7.batch.chunk.exception;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.List;

/**
 * @author Arun Gupta
 */
@Named
public class MyItemWriter extends AbstractItemWriter {
    private static int retries = 0;

    @Inject
    JobContext ctx;

    @Override
    public void writeItems(List list) {

        System.out.println("MyItemWriter.writeItems: " + list);

        if (retries <= 3 && list.contains(new MyOutputRecord(8))) {
            retries ++;
            System.out.println("Throw UnsupportedOperationException in MyItemWriter");
            throw new UnsupportedOperationException();
        }
        updateExitStatus(list);
    }

    /**
     * For each record in list, appends id, plus ",", to current exit status
     * @param list
     */
    private void updateExitStatus(List list) {
        String es = ctx.getExitStatus() == null ? "" : ctx.getExitStatus();
        StringBuilder sb = new StringBuilder(es);

        for (Object o : list) {
            MyOutputRecord rec = (MyOutputRecord)o;
            sb.append(rec.getId()).append(",");
        }

        System.out.println("MyItemWriter.updateExitStatus: " + sb.toString());
        ctx.setExitStatus(sb.toString());
    }
}
