/*
 * Copyright 2013-2025 the HotswapAgent authors.
 *
 * This file is part of HotswapAgent.
 *
 * HotswapAgent is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 2 of the License, or (at your
 * option) any later version.
 *
 * HotswapAgent is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with HotswapAgent. If not, see http://www.gnu.org/licenses/.
 */
package org.hotswap.agent.plugin.spring.boot.env.v2;

import org.hotswap.agent.logging.AgentLogger;
import org.hotswap.agent.plugin.spring.boot.env.BasePropertiesPropertySourceLoader;
import org.hotswap.agent.plugin.spring.boot.env.ListPropertySourceReloader;
import org.hotswap.agent.util.ReflectionHelper;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

public class YamlPropertySourceLoader extends BasePropertiesPropertySourceLoader<List<Map<String, Object>>> {

    private static AgentLogger LOGGER = AgentLogger.getLogger(YamlPropertySourceLoader.class);

    private Resource resource;

    public YamlPropertySourceLoader(String name, Resource resource) {
        super(new ListPropertySourceReloader(name, resource));
        this.resource = resource;
    }

    protected List<Map<String, Object>> doLoad() {
        try {
            Object yamlLoader = ReflectionHelper.invokeConstructor(
                    "org.springframework.boot.env.OriginTrackedYamlLoader",
                    this.getClass().getClassLoader(), new Class[]{Resource.class}, resource);
            return (List<Map<String, Object>>) ReflectionHelper.invoke(yamlLoader, "load");
        } catch (Exception e) {
            LOGGER.error("load yaml error, resource: {}", e, resource);
        }
        return null;
    }
}
