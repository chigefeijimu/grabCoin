package heco.com.heco.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {
//    private static final Logger logger = LoggerFactory.getLogger(NoModelDataListener.class);
//
//    private static final int BATCH_COUNT = 3000;
//    List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);
    }
}
