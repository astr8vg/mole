package com.zd.mole.task;

public enum TaskStatus {
	
	//准备
	Ready,
	//准备下载
	ReadyToDownload,
	//下载中
	Downloading,
	//下载完成
	Downloaded,
	//下载失败
	DownloadFaild,
	//准备解析
	ReadyToParse,
	//解析失败，找不到文件
	ParseFileNotFound,
	//解析中
	Parseing,
	//保存三方库中
	SaveingToThird,
	//保存三方库失败
	SaveFailed,
	//保存三方库成功
	SaveSucceed
}
