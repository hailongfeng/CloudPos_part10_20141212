package com.cynovo.sirius.core;

import java.util.List;

import com.cynovo.sirius.core.order.OrderContent;

public interface ISaleView {

	public void setup();
	public void setItems(List<OrderContent> mList);
	public void addItems(List<OrderContent> mList);
	public void updateItems(List<OrderContent> mList);
	public void removeItems(List<OrderContent> mList);
	public void removeAllItems();
}


