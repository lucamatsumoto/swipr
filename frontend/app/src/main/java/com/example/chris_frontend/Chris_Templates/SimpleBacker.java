package com.example.chris_frontend.Chris_Templates;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleBacker {
	
	protected final List<SimpleCommandList> mAllList;
	
	protected SimpleBacker(List<SimpleCommandList> allList){mAllList = allList;}
	
	public SimpleCommandList get(int k){return mAllList.get(k);}
	
	public abstract SimpleBacker getInstance();
	
	public static class SimpleCommandList {
		protected List<Object> mCommandList;
		protected int mIndex;
		public SimpleCommandList()
		{
			mCommandList = new ArrayList();
			mIndex =0;
		}
		public Object get(int k){return mCommandList.get(k);}
		public int size(){return mCommandList.size();}
		public void add(Object object){mCommandList.add(object);}
		public void remove(int k){mCommandList.remove(k);}
		public void clear(){mCommandList.clear();}
		public int getIndex(){return mIndex;}
		public boolean setIndex(int k)
		{
			if(k >= mCommandList.size())
				return false;
			mIndex = k;
			return true;
		}
	}
}