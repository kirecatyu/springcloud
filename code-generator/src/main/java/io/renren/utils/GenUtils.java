/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.utils;

import io.renren.entity.ColumnEntity;
import io.renren.entity.TableEntity;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午11:40:24
 */
public class GenUtils {

	public static List<String> getTemplates(){
		List<String> templates = new ArrayList<String>();
		templates.add("template/Entity.java.vm");
		templates.add("template/Vo.java.vm");
		templates.add("template/Dto.java.vm");
		templates.add("template/Dao.java.vm");
		templates.add("template/Dao.xml.vm");
		templates.add("template/Service.java.vm");
		templates.add("template/ServiceImpl.java.vm");
		templates.add("template/Controller.java.vm");
		templates.add("template/List.html.vm");
		templates.add("template/List.js.vm");
		templates.add("template/menu.sql.vm");
		templates.add("template/View.html.vm");
		templates.add("template/Edit.html.vm");
		templates.add("template/Edit.js.vm");
		templates.add("template/makeit.bat.vm");
		templates.add("template/clear.bat.vm");
		return templates;
	}

	/**
	 * 生成代码
	 */
	public static void generatorCode(Map<String, String> table,
			List<Map<String, String>> columns, ZipOutputStream zip){
		//配置信息
		Configuration config = getConfig();
		boolean hasBigDecimal = false;
		//表信息
		TableEntity tableEntity = new TableEntity();
		tableEntity.setTableName(table.get("tableName"));
		tableEntity.setComments(table.get("tableComment"));
		//表名转换成Java类名
		String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
		tableEntity.setClassName(className);
		tableEntity.setClassname(StringUtils.uncapitalize(className));

		//列信息
		List<ColumnEntity> columsList = new ArrayList<>();
		for(Map<String, String> column : columns){
			ColumnEntity columnEntity = new ColumnEntity();
			columnEntity.setColumnName(column.get("columnName"));
			columnEntity.setDataType(column.get("dataType"));
			columnEntity.setComments(column.get("columnComment"));
			columnEntity.setExtra(column.get("extra"));

			//列名转换成Java属性名
			String attrName = columnToJava(columnEntity.getColumnName());
			columnEntity.setAttrName(attrName);
			columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

			//列的数据类型，转换成Java类型
			String attrType = config.getString(columnEntity.getDataType(), "unknowType");
			columnEntity.setAttrType(attrType);
			if (!hasBigDecimal && attrType.equals("BigDecimal" )) {
				hasBigDecimal = true;
			}
			//是否主键
			if("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null){
				tableEntity.setPk(columnEntity);
			}

			columsList.add(columnEntity);
		}
		tableEntity.setColumns(columsList);

		//没主键，则第一个字段为主键
		if(tableEntity.getPk() == null){
			tableEntity.setPk(tableEntity.getColumns().get(0));
		}

		//设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(prop);

		String mainPath = config.getString("mainPath" );
		mainPath = StringUtils.isBlank(mainPath) ? "io.renren" : mainPath;

		//封装模板数据
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", tableEntity.getTableName());
		map.put("comments", tableEntity.getComments());
		map.put("pk", tableEntity.getPk());
		map.put("className", tableEntity.getClassName());
		map.put("classname", tableEntity.getClassname());
		map.put("pathName", tableEntity.getClassname());
		map.put("columns", tableEntity.getColumns());
		map.put("hasBigDecimal", hasBigDecimal);
		map.put("mainPath", mainPath);
		String _package=config.getString("package" );
		String packageDir=StringUtils.replaceEach(_package,new String[]{"."},new String[]{"\\"});
		String tempArr[]=_package.split("\\.");
		String projectName=tempArr[tempArr.length-1];
		map.put("package", _package);
		map.put("packageDir", packageDir);
		map.put("projectName", projectName);

		map.put("moduleName", config.getString("moduleName" ));
		map.put("projectDir", config.getString("projectDir" ));

		Map<String,String> systemMap = System.getenv();
		System.out.println(systemMap.get("USER"));//获取用户名
		System.out.println(systemMap.get("USERNAME"));//获取用户名
		String author = null;
		if(StringUtils.isNotBlank(systemMap.get("USERNAME"))){
			author=systemMap.get("USERNAME");
		}else if(StringUtils.isNotBlank(systemMap.get("USER"))){
			author=systemMap.get("USER");
		}
		map.put("author", author);
		map.put("email", config.getString("email"));
		map.put("dataSource", config.getString("dataSource"));
		map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
		List<String> templates = getTemplates();
		for(String template : templates){
			//渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, "UTF-8");
			tpl.merge(context, sw);

			try {
				//添加到zip
				zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"))));
				IOUtils.write(sw.toString(), zip, "UTF-8");
				IOUtils.closeQuietly(sw);
				zip.closeEntry();
			} catch (IOException e) {
				e.printStackTrace();
//				throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
			}
		}
	}


	/**
	 * 列名转换成Java属性名
	 */
	public static String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
	}

	/**
	 * 表名转换成Java类名
	 */
	public static String tableToJava(String tableName, String tablePrefix) {
		if(StringUtils.isNotBlank(tablePrefix)){
			tableName = tableName.replace(tablePrefix, "");
		}
		return columnToJava(tableName);
	}

	/**
	 * 获取配置信息
	 */
	public static Configuration getConfig(){
		try {
			return new PropertiesConfiguration("generator.properties");
		} catch (ConfigurationException e) {
			throw new RRException("获取配置文件失败，", e);
		}
	}

	/**
	 * 获取文件名
	 */
	public static String getFileName(String template, String className, String packageName, String moduleName) {
		String packagePath = "main" + File.separator + "java" + File.separator;
		if (StringUtils.isNotBlank(packageName)) {
			packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
		}

		if (template.contains("Entity.java.vm" )) {
			return packagePath + "entity" + File.separator + className + "Entity.java";
		}
		if (template.contains("Vo.java.vm" )) {
			return packagePath + "vo" + File.separator + className + "Vo.java";
		}
		if (template.contains("Dto.java.vm" )) {
			return packagePath + "dto" + File.separator + className + "Dto.java";
		}

		if (template.contains("Dao.java.vm" )) {
			return packagePath + "dao" + File.separator + className + "Mapper.java";
		}

		if (template.contains("Service.java.vm" )) {
			return packagePath + "service" + File.separator + className + "Service.java";
		}

		if (template.contains("ServiceImpl.java.vm" )) {
			return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
		}

		if (template.contains("Controller.java.vm" )) {
			return packagePath + "controller" + File.separator + className + "Controller.java";
		}

		if (template.contains("Dao.xml.vm" )) {
			return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Dao.xml";
		}

		if (template.contains("List.html.vm" )) {
			return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
					+ "modules" + File.separator + moduleName + File.separator + className.replaceFirst(className.substring(0,1), className.substring(0,1).toLowerCase()) + "List.html";
		}
		if (template.contains("View.html.vm" )) {
			return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
					+ "modules" + File.separator + moduleName + File.separator + className.replaceFirst(className.substring(0,1), className.substring(0,1).toLowerCase()) + "View.html";
		}

		if (template.contains("List.js.vm" )) {
			return "main" + File.separator + "resources" + File.separator + "statics" + File.separator + "js" + File.separator
					+ "modules" + File.separator + moduleName + File.separator + className.replaceFirst(className.substring(0,1), className.substring(0,1).toLowerCase()) + "List.js";
		}
		if (template.contains("Edit.js.vm" )) {
			return "main" + File.separator + "resources" + File.separator + "statics" + File.separator + "js" + File.separator
					+ "modules" + File.separator + moduleName + File.separator + className.replaceFirst(className.substring(0,1), className.substring(0,1).toLowerCase()) + "Edit.js";
		}
		if (template.contains("Edit.html.vm" )) {
			return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
					+ "modules" + File.separator + moduleName + File.separator + className.replaceFirst(className.substring(0,1), className.substring(0,1).toLowerCase()) + "Edit.html";
		}

		if (template.contains("menu.sql.vm" )) {
			return className.toLowerCase() + "_menu.sql";
		}

		if(template.contains("makeit.bat.vm")){
			return "makeit.bat";
		}

		if(template.contains("clear.bat.vm")){
			return "clear.bat";
		}

		return null;
	}
}
