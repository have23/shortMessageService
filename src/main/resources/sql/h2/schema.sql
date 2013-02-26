    drop table if exists spponepay_order;
    create table spponepay_order (
        id varchar(255),
		transdate varchar(255),	              --创建支付订单交易日期
		transtime varchar(255),	              --创建支付订单交易时间
		settledate varchar(255),	          --支付订单清算日期
		mall_id varchar(255),		          --商圈的ID
		mid varchar(255),			          --商户编号
		midname varchar(255),		          --商户名字
		tid varchar(255),			          --子商户或者终端代码
	    sp_id varchar(255),			          --sp_id
	    user_id	varchar(255),	              --电力系统用户唯一编号user_id
		order_mid varchar(255),	              --商圈订单编号
		order_pay varchar(255),	              --支付订单编号
		submittime varchar(255),              --支付订单时间戳
		transamount NUMERIC(18,2),	          --交易金额,不满12位前补“0”
	 	transamount_currency varchar(255),	  --交易币种
		billamount  NUMERIC(18,2),			  --扣账金额,不满12位前补“0”
		billamount_currency varchar(255),     --账户币种
	 	billamount_convertrate NUMERIC(18,2), --扣账汇率
	    accountnumber1 varchar(255),		  --付款人账户
	    transserialnumber varchar(255),		  --交易流水号
		sts_order_pay varchar(255),		 	  --支付订单状态 0未支付；1支付失败；2支付成功；3交易关闭(未完成支付)；4撤销交易；5退货
	    sts varchar(255),					  --本条记录状态流程对应下面发起交易
	    req_create_json varchar(255),		  --请求创建订单的json串
	    res_create_json varchar(255),		  --应答创建订单的json串
		req_advise_json varchar(255),		  --请求订单支付成功通知的json串
		res_advise_json varchar(255),		  --应答订单支付成功通知的json串
	    req_query_json  varchar(255),	      --请求查询订单的json串
	    res_query_json  varchar(255),		  --应答查询订单的json串	
	    resv1 varchar(255),				      --备注resv1
	    resv2 varchar(255),				      --备注resv2
	    resv3 varchar(255),				      --备注resv3
	    resv4 varchar(255),				      --备注resv4
        primary key (id)
    );