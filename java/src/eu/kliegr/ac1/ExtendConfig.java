/*
 * Monotonicity Exploiting Association Rule Classification (MARC)
 *
 *     Copyright (C)2014-2017 Tomas Kliegr
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.kliegr.ac1;

import eu.kliegr.ac1.data.AttributeType;
import eu.kliegr.ac1.rule.MMACRuleComparator;
import eu.kliegr.ac1.rule.extend.ExtendType;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Properties;
import java.util.logging.Logger;

public class ExtendConfig extends BaseConfig {
    private final static Logger LOGGER = Logger.getLogger(ExtendConfig.class.getName()); 
    private String rulesPath = "/home/tomas/NetBeansProjects/AC1/resources/Iris1_iris_1_0.5.xml";
    private String extendRuleSortComparator ="MMACRuleComparator";
    private boolean annotate = true;
    private boolean pruneAfterExtend = true;
    private boolean continuosPruning= true;
    private boolean performFuzzification = false;

    private Comparator ruleComparator ;
    private ExtendType extendType;

    /**
     *
     */
    public ExtendConfig() {
    }

    /**
     *
     * @param attType
     * @param targetAttribute
     * @param IDcolumnName
     */
    public ExtendConfig(ArrayList<AttributeType> attType, String targetAttribute, String IDcolumnName) {
        ruleComparator=new MMACRuleComparator();
        extendType=ExtendType.numericOnly;
        annotate=false;
        pruneAfterExtend=true;
        continuosPruning=false;
        performFuzzification=false;
        this.attType= attType;
        this.targetAttribute = targetAttribute;
        this.IDcolumnName=IDcolumnName;
    }

    /**
     *
     * @param path
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public ExtendConfig(String path) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        InputStream input = new BufferedInputStream(
                new FileInputStream(path));
        Properties prop = new Properties();
        prop.loadFromXML(input);
        rulesPath = prop.getProperty("RulesPath");
        dataPath = prop.getProperty("TrainDataPath");
        extendRuleSortComparator = prop.getProperty("ExtendRuleSortComparator");
        ruleComparator = (Comparator) Class.forName("eu.kliegr.ac1.rule." +extendRuleSortComparator).newInstance();
        extendType= ExtendType.valueOf(prop.getProperty("ExtendType"));
        String _ann =prop.getProperty("Annotate");
        if (_ann != null) {
            annotate= Boolean.valueOf(_ann);
        }
        String _prune =prop.getProperty("PruneAfterExtend");
        if (_prune != null) {
            pruneAfterExtend= Boolean.valueOf(_prune);
        }
        String _prune_cont =prop.getProperty("ContinuousPruning");
        if (_prune_cont != null) {
            continuosPruning= Boolean.valueOf(_prune_cont);
        }
        String _fuzz =prop.getProperty("Fuzzification");
        if (_fuzz != null) {
            performFuzzification= Boolean.valueOf(_prune_cont);
        }
        this.setOutputPath(prop.getProperty("OutputPath"));
        if (prop.getProperty("DataTypes").contains(";"))
        {
            csvSeparator =  ";";
        }
        else
        {
            csvSeparator =  ",";
        }
        attType = parseAttributeTypes(prop.getProperty("DataTypes").split(csvSeparator));
        targetAttribute = prop.getProperty("TargetAttribute");
        IDcolumnName = prop.getProperty("IDcolumnName");
    }

    /**
     *
     * @return
     */
    public Comparator getRuleComparator()
    {
        return ruleComparator;
    }

    /**
     *
     * @return
     */
    public Boolean isAnnotationEnabled()
    {
        return annotate;
    }  

    /**
     *
     * @return
     */
    public Boolean isContinuousPruningEnabled()
    {
        return continuosPruning;
    }  

    /**
     *
     * @return
     */
    public Boolean isPostPruningEnabled()
    {
        return pruneAfterExtend;
    }    

    /**
     *
     * @return
     */
    public Boolean isFuzzificationEnabled()
    {
        return performFuzzification;
    }  

    /**
     *
     * @return
     */
    public String getRulesPath()
    {
        return rulesPath;
    }

    /**
     *
     * @return
     */
    public String getOutputSeedRulesPath()
        {
            return getOutputPath("seed");
        }

    /**
     *
     * @return
     */
    public String getOutputExtendedRulesPath()
        {
            return getOutputPath("arules");
        }

    /**
     *
     * @return
     */
    public String getOutputSummaryPath()
        {
            return getOutputPath("summary");
        }

    /**
     *
     * @return
     */
    public ExtendType getExtendType()
    {
        return extendType;
    }

    
}
