/*
 * Copyright 2017 Credit Mutuel Arkea
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.arkea.satd.sonar.xml.checks;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.plugins.xml.checks.AbstractXmlCheck;
import org.sonar.plugins.xml.checks.XmlFile;
import org.sonar.plugins.xml.checks.XmlSourceCode;
import org.sonar.plugins.xml.compat.CompatibleInputFile;
import org.sonar.plugins.xml.parsers.ParseException;

import com.arkea.satd.sonar.xml.AbstractXmlPluginTester;

import static org.sonar.plugins.xml.compat.CompatibilityHelper.wrap;

public abstract class AbstractCheckTester extends AbstractXmlPluginTester {

  @org.junit.Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  
  private DefaultFileSystem filesystem = null;

  XmlSourceCode parseAndCheck(File file, AbstractXmlCheck check) {
    XmlSourceCode xmlSourceCode = new XmlSourceCode(new XmlFile(newInputFile(file), createFileSystem()));

    try {
    	xmlSourceCode.parseSource();
    	check.validate(xmlSourceCode);
	} catch(ParseException e) {
		// Do nothing
	}

    return xmlSourceCode;
  }

  XmlSourceCode parse(File file) {
	    XmlSourceCode xmlSourceCode = new XmlSourceCode(new XmlFile(newInputFile(file), createFileSystem()));
	    return xmlSourceCode;
	  }  
  
  private CompatibleInputFile newInputFile(File file) {
    return wrap(new DefaultInputFile("modulekey", file.getName())
      .setModuleBaseDir(file.getParentFile().toPath())
      .setCharset(StandardCharsets.UTF_8));
  }

  protected DefaultFileSystem createFileSystem() {
	  
	  if(filesystem!=null) {
		  return filesystem;
	  }

	try {
		File workDir = temporaryFolder.newFolder("temp");
	
	    filesystem = new DefaultFileSystem(workDir);
	    filesystem.setEncoding(Charset.defaultCharset());
	    filesystem.setWorkDir(workDir);
	
	    return filesystem;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	}
	return null;
    
  }

  File createTempFile(String content) throws IOException {
	  
    //File f = temporaryFolder.newFile("file.xml");
	File f = File.createTempFile("temp-file-name", ".xml", createFileSystem().workDir());
    FileUtils.write(f, content, Charset.defaultCharset());

    return f;
  }
  
  File createTempFile(String content, String fileName, String directory) throws IOException {
	  
	    File tempRoot = createFileSystem().workDir();
	    File subDir = new File(tempRoot, directory);
	    subDir.mkdir();
	    
		File f = File.createTempFile(fileName.substring(0, fileName.indexOf('.')), fileName.substring(fileName.indexOf('.')), subDir);
	    FileUtils.write(f, content, Charset.defaultCharset());

	    return f;
	  }
	    

}
