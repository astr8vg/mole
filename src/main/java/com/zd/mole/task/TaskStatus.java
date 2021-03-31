package com.zd.mole.task;

public enum TaskStatus {
	
	//准备
	Ready,
	//待处理
	ReadyToDispose,
	//处理中
	Disposing,
	//保存失败
	SaveFailed,
	//保存成功
	SaveSucceed
}
