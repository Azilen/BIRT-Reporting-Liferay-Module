package com.azilen.birt.utiil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
public class ParametersTask {

	private static Logger  LOG = Logger.getLogger(ParametersTask.class);

	public static HashMap<String, HashMap<String, Serializable>> getParametersFromReport(String reportName,String birtHome,String reportDesignHome,String logPath) throws EngineException
	{
		HashMap<String, HashMap<String, Serializable>> parmDetails = new HashMap<String, HashMap<String, Serializable>>();

		IReportEngine engine=null;
		EngineConfig config = null;
		try{
			config = new EngineConfig();
			config.setEngineHome(birtHome);
			config.setLogConfig(logPath, Level.FINE);
			Platform.startup(config);
			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
			engine = factory.createReportEngine(config);
			engine.changeLogLevel(Level.WARNING);

		}catch(BirtException ex){
			LOG.error("BirtException while birt engine platform startup ",ex);
		}

		IReportRunnable design = null;

		//Open a report design 
		design = engine.openReportDesign(reportDesignHome+reportName); 

		//Create Parameter Definition Task and retrieve parameter definitions
		IGetParameterDefinitionTask task = engine.createGetParameterDefinitionTask( design );
		Collection<IParameterDefnBase> params = (Collection<IParameterDefnBase>)task.getParameterDefns( true );

		//Iterate over each parameter
		for ( IParameterDefnBase param : params )
		{
			if ( param instanceof IParameterGroupDefn )
			{
				IParameterGroupDefn group = (IParameterGroupDefn) param;

				// Do something with the parameter group.
				// Iterate over group contents.
				Iterator i2 = group.getContents( ).iterator( );
				while (i2.hasNext( ))
				{
					IScalarParameterDefn scalar = (IScalarParameterDefn) i2.next( );
					//Get details on the parameter
					parmDetails.put( scalar.getName(), loadParameterDetails( task, scalar, design, group));
				}
			}
			else
			{
				IScalarParameterDefn scalar = (IScalarParameterDefn) param;
				//get details on the parameter
				parmDetails.put( scalar.getName(),loadParameterDetails( task, scalar, design, null));                   
			}
		}

		//Destroy the engine and shutdown the Platform
		//Note - If the program stays resident do not shutdown the Platform or the Engine  
		engine.destroy();
		Platform.shutdown();
		LOG.info("Finished"+parmDetails.toString());
		return parmDetails;
	}

	//Function to load parameter details in a map.
	private static HashMap<String, Serializable> loadParameterDetails(IGetParameterDefinitionTask task, IScalarParameterDefn scalar, IReportRunnable report, IParameterGroupDefn group)
	{

		HashMap<String, Serializable> parameter = new HashMap<String, Serializable>();

		parameter.put("Parameter Group", group == null ? "Default" : group.getName());   
		parameter.put("Name", scalar.getName());
		parameter.put("Help Text", scalar.getHelpText());
		parameter.put("Display Name", scalar.getDisplayName());
		//this is a format code such as  > for UPPERCASE
		parameter.put("Display Format", scalar.getDisplayFormat());

		parameter.put("Hidden", scalar.isHidden() ? "Yes" : "No");
		parameter.put("Allow Blank", scalar.allowBlank() ? "Yes" : "No");
		parameter.put("Allow Null", scalar.allowNull() ? "Yes" : "No");
		parameter.put("Conceal Entry", scalar.isValueConcealed() ? "Yes" : "No");  //ie passwords etc


		switch (scalar.getControlType()) {
		case IScalarParameterDefn.TEXT_BOX:  parameter.put("Type", "Text Box"); break;
		case IScalarParameterDefn.LIST_BOX:  parameter.put("Type", "List Box"); break;
		case IScalarParameterDefn.RADIO_BUTTON:  parameter.put("Type", "List Box"); break;
		case IScalarParameterDefn.CHECK_BOX:  parameter.put("Type", "List Box"); break;
		default: parameter.put("Type", "Text Box");break;
		}


		switch (scalar.getDataType()) {
		case IScalarParameterDefn.TYPE_STRING:  parameter.put("Data Type", "String"); break;
		case IScalarParameterDefn.TYPE_FLOAT:  parameter.put("Data Type", "Float"); break;
		case IScalarParameterDefn.TYPE_DECIMAL:  parameter.put("Data Type", "Decimal"); break;
		case IScalarParameterDefn.TYPE_INTEGER:  parameter.put("Data Type", "Integer"); break;
		case IScalarParameterDefn.TYPE_DATE:  parameter.put("Data Type", "Date"); break;
		case IScalarParameterDefn.TYPE_DATE_TIME:  parameter.put("Data Type", "Date Time"); break;
		case IScalarParameterDefn.TYPE_BOOLEAN:  parameter.put("Data Type", "Boolean"); break;
		default:  parameter.put("Data Type", "Any"); break;
		}


		//Get report design and find default value, prompt text and data set expression using the DE API
		ReportDesignHandle reportHandle = ( ReportDesignHandle ) report.getDesignHandle( );
		ScalarParameterHandle parameterHandle = ( ScalarParameterHandle ) reportHandle.findParameter( scalar.getName() );
		parameter.put("Default Value", parameterHandle.getDefaultValue());
		parameter.put("Prompt Text", parameterHandle.getPromptText());
		parameter.put("Data Set Expression", parameterHandle.getValueExpr());

		if(scalar.getControlType() !=  IScalarParameterDefn.TEXT_BOX)
		{
			//retrieve selection list for cascaded parameter
			if ( parameterHandle.getContainer( ) instanceof CascadingParameterGroupHandle ){
				if ( parameterHandle.getContainer( ) instanceof CascadingParameterGroupHandle )
				{
					int index = parameterHandle.getContainerSlotHandle( ).findPosn( parameterHandle );
					Object[] keyValue = new Object[index];
					for ( int i = 0; i < index; i++ )
					{
						ScalarParameterHandle handle = (ScalarParameterHandle) ( (CascadingParameterGroupHandle) parameterHandle.getContainer( ) ).getParameters( ).get( i );
						//Use parameter default values
						keyValue[i] = handle.getDefaultValue();
					}
					String groupName = parameterHandle.getContainer( ).getName( );
					task.evaluateQuery( groupName );

					Collection<IParameterSelectionChoice> sList = (Collection<IParameterSelectionChoice>)task.getSelectionListForCascadingGroup( groupName, keyValue );
					HashMap<Object, String> dynamicList = new HashMap<Object, String>();       


					for ( IParameterSelectionChoice sI : sList )
					{
						Object value = sI.getValue( );
						Object label = sI.getLabel( );
						LOG.info(" Label and Value : "+ label + "--" + value);
						dynamicList.put(value,(String) label);

					}         
					parameter.put("Selection List", dynamicList);


				}         
			}else{
				//retrieve selection list
				Collection<IParameterSelectionChoice> selectionList = (Collection<IParameterSelectionChoice>)task.getSelectionList( scalar.getName() );

				if ( selectionList != null )
				{
					HashMap<Object, String> dynamicList = new HashMap<Object, String>();       

					for ( IParameterSelectionChoice selectionItem : selectionList )
					{
						Object value = selectionItem.getValue( );
						String label = selectionItem.getLabel( );
						//System.out.println( label + "--" + value);
						dynamicList.put(value,label);

					}
					parameter.put("Selection List", dynamicList);
				}
			}

		}


		//Print out results
		// please check this method for the parameter values
		/*System.out.println("======================Parameter =" + scalar.getName());
		for (Map.Entry<String, Serializable> entry : parameter.entrySet( ) {
			String name = entry.getKey(); 
			if( name.equals("Selection List")){
				HashMap<?,?> selList = (HashMap<?,?>)entry.getValue();
				for (Map.Entry<?,?> entry : selList.entrySet()) {
					System.out.println( "Selection List Entry ===== Key = " + entry.getKey() + " Value = " + entry.getValue());
				}

			}else{
				System.out.println( name + " = " + entry.getValue());     
			}
		}*/

		LOG.info("Parameter "+parameter.toString());
		return parameter;
	}

}