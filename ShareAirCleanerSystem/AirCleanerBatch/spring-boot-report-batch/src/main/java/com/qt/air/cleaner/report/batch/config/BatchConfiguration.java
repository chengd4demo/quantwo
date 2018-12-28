package com.qt.air.cleaner.report.batch.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.qt.air.cleaner.report.batch.model.Billing;
import com.qt.air.cleaner.report.batch.model.ScanCodeAmount;
import com.qt.air.cleaner.report.batch.processor.RecordProcessor;
import com.qt.air.cleaner.report.batch.service.ScanCodeAmountService;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
	private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private JobBuilderFactory jobs;
	@Autowired
	private Step steps;
	@Autowired
	private JobExecutionListener listener;
	@Autowired
	private ScanCodeAmountService scanCodeAmountService;
	
	public void run() {
		try {
			String dateParam = new Date().toString();
			JobParameters param = new JobParametersBuilder().addString("date", dateParam).toJobParameters();
			System.out.println(dateParam);
			ScanCodeAmount  scanCodeAmount = scanCodeAmountService.findByDeviceName("866289030055174", new Date());
			logger.info("查询结果：{}",scanCodeAmount.toString());
			//执行job
			/*JobExecution execution = jobLauncher.run(importScanCodeAmountJob(jobs,steps,listener), param); 
			System.out.println("Exit Status : " + execution.getStatus());*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bean
	public ItemReader<Billing> reader(DataSource dataSource) {
		JdbcCursorItemReader<Billing> reader = new JdbcCursorItemReader<>();
		//List<ScanCodeAmount> scanCodeAmountList = scanCodeAmountService.findAll();
		StringBuffer sql = new StringBuffer();
		sql.append("select d.mach_no,to_char(b.create_time, 'YYYY-MM-DD') as create_time, count(b.id) as count,tmp.tradername,tmp.investorname from mk_device d");
		sql.append(" left join act_billing b on d.id = b.device_id");
		sql.append(" inner join (select tr.name as traderName, inv.name as investorName, d.id");
		sql.append(" from mk_device d");
		sql.append(" left join mk_trader tr on d.trader_id = tr.id");
		sql.append(" left join mk_investor inv on d.investor_id = inv.id");
		sql.append(" where tr.name is not null");
		sql.append(" and inv.name is not null) tmp");
		sql.append(" on tmp.id = d.id");
		sql.append(" where d.removed = 'N'");
		sql.append(" group by d.mach_no, tmp.traderName, tmp.investorName, to_char(b.create_time, 'YYYY-MM-DD')   order by to_char(b.create_time, 'YYYY-MM-DD')");
		reader.setSql(sql.toString());
		 //若扫码统计表里面数据不cun
		/*if(!CollectionUtils.isEmpty(scanCodeAmountList)) {
			reader.setPreparedStatementSetter(new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					// TODO Auto-generated method stub
					
				}
			});
		}*/
		System.out.println(sql.toString());
		reader.setDataSource(dataSource);
		reader.setRowMapper(
                (ResultSet resultSet, int rowNum) -> {
                    if (!(resultSet.isAfterLast()) && !(resultSet.isBeforeFirst())) {
                    	Billing billing = new Billing();
                    	billing.setMachNo(resultSet.getString("mach_no"));
                    	if(!StringUtils.isEmpty(resultSet.getString("create_time"))){
	                    	try {
	                    			billing.setCreateTime(DF.parse(resultSet.getString("create_time")));
	                    		} catch (ParseException e) {
								e.printStackTrace();
							}
                    	}
                    	billing.setCount(resultSet.getInt("count"));
                    	billing.setTraderName(resultSet.getString("traderName"));
                    	billing.setInvestorName(resultSet.getString("investorName"));
                        return billing;
                    } else {
                    	logger.info("Returning null from rowMapper");
                        return null;
                    }
                });
		return reader;
	}
	
	@Bean
	 public ItemProcessor<Billing, ScanCodeAmount> processor() {
        return new RecordProcessor();
    }
	
	@Bean
	public ItemWriter<ScanCodeAmount> writer(DataSource dataSource,
			ItemPreparedStatementSetter<ScanCodeAmount> setter) {
		JdbcBatchItemWriter<ScanCodeAmount> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.setItemPreparedStatementSetter(setter);
		writer.setSql(
				"insert into rep_scan_code_amount (id,device_name, trader_name,investor_name,count,billing_time,create_time) values (sys_guid(),?,?,?,?,?,sysdate)");
		writer.setDataSource(dataSource);
		return writer;
	}
	
	
	@Bean
    public ItemPreparedStatementSetter<ScanCodeAmount> setter() {
        return (item, ps) -> {
            ps.setString(1, item.getDeviceName());
            ps.setString(2, item.getTraderName());
            ps.setString(3, item.getInvestorName());
            ps.setLong(4, item.getCount());
            if (StringUtils.isEmpty(item.getBillingTime())) {
            	  ps.setDate(5, new java.sql.Date(new Date().getTime()));
            } else {
            	  ps.setDate(5, new java.sql.Date(item.getBillingTime().getTime()));
            }
        };
    }

	@Bean
	public Job importScanCodeAmountJob(JobBuilderFactory jobs, Step s1, JobExecutionListener listener) {
		return jobs.get("importScanCodeAmountJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(s1)
                .end()
                .build();
	}
	
	@Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Billing> reader,
                      ItemWriter<ScanCodeAmount> writer, ItemProcessor<Billing, ScanCodeAmount> processor) {
        return stepBuilderFactory.get("step1")
                .<Billing, ScanCodeAmount>chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
	/*@Bean
	public Step step2(StepBuilderFactory stepBuilderFactory, ItemReader<Billing> reader,
             ItemWriter<ScanCodeAmount> writer, ItemProcessor<Billing, ScanCodeAmount> processor) {
		return stepBuilderFactory.get("step2")
		       .<Billing, ScanCodeAmount>chunk(5)
		       .reader(scanCodeAmountService.findAll())
		       .processor(processor)
		       .writer(writer)
		       .build();
	}*/
	
	@Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
