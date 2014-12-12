package com.cynovo.sirius.core;

import java.util.List;
import com.cynovo.sirius.core.order.OrderContentData;

public interface IProductSelector {
	void addNewOrderContent(List<OrderContentData> mList);
}