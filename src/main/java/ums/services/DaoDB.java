/*******************************************************************************
 * 使用JPA的@PersistenceContext的标签获得实体管理器，
 * 完成执行原生sql语句和存储过程功能。
*******************************************************************************/
package ums.services;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("daodb")
public class DaoDB {
	//生成日志
	private static Logger logger = LoggerFactory.getLogger(DaoDB.class);
	//通过spring注入实体管理类
	@PersistenceContext  
	public EntityManager em;
	
	//执行原生sql语句，返回对象数组(Object[])列表的结果集，
	//但是对账数组的单元不能统一强制转换为String类型，否则会报转换类型错误，
	//只能使用Object.toString()函数获得字符串形式，具体结果依赖各种对象的实现，
	//并且如果仅仅返回一列，也不能统一转换，所以只有由执行sql语句的函数根据sql语句具体转换对象
	@Transactional
	public List querySql(String sql){
		Query query = null;
		List objecArraytList = null;
		logger.info("querysql执行语句[" +sql +"]");
		//创建原生SQL查询QUERY实例
		query =  em.createNativeQuery(sql);
		objecArraytList = query.getResultList();
		return objecArraytList;
	}
	
	//根据表格名字、字段名字、字段内容删除数据
	@Transactional
	public int deletetable(String tablename, String columnname, String columnvalue ){
		Query query;
		String exestr=" delete from " +tablename +" where " +columnname +"='" +columnvalue +"'";
		logger.info("execute执行语句处理开始[" +exestr +"]");
		query = em.createNativeQuery(exestr);
		int res = query.executeUpdate();
		logger.info("execute执行语句处理开始成功。" );
		return res;
		
	}

	//执行存储过程
	@Transactional
	public void call(String procname){
		Query query;
		String calltstr=" {call " +procname +"()} ";
		logger.info("call调用存储过程[" +calltstr +"]");
		query = em.createNativeQuery(calltstr);
		query.executeUpdate();
	}
	@Transactional
	public void call(String procname, String settledate){
		Query query;
		String calltstr=" {call " +procname +"(?)} ";
		logger.info("call调用存储过程[" +calltstr +"]" +settledate);
		query = em.createNativeQuery(calltstr);
		query.setParameter(1, settledate);
		query.executeUpdate();
	}
	@Transactional
	public void call(String procname, String begdate, String enddate){
		Query query;
		String calltstr=" {call " +procname +"(?,?)} ";
		logger.info("call调用存储过程[" +calltstr +"]" +begdate +"," +enddate);
		query = em.createNativeQuery(calltstr);
		query.setParameter(1, begdate);
		query.setParameter(2, enddate);
		query.executeUpdate();
	}
}

