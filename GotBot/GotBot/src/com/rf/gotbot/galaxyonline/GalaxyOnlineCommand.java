/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.galaxyonline;

import com.rf.gotbot.gameplay.CommandCycle;

/**
 *
 * @author REx
 */
public enum GalaxyOnlineCommand implements CommandCycle
{
    HARVEST_RESOURCES()
    {
        @Override
        public String currentCommand()
        {
            return name();
        }

        @Override
        public CommandCycle onFail()
        {
            return this;
        }

        @Override
        public CommandCycle onSuccess() 
        {
            return CHECK_MAIL_FOR_INSTANCE;
        }
    },
    CHECK_MAIL_FOR_INSTANCE()
    {
        @Override
        public String currentCommand()
        {
            return name();
        }

        @Override
        public CommandCycle onFail()
        {
            return this;
        }

        @Override
        public CommandCycle onSuccess() 
        {
            return DO_INSTANCE;
        }
    },
    DO_INSTANCE()
    {
        @Override
        public String currentCommand()
        {
            return name();
        }

        @Override
        public CommandCycle onFail()
        {
            return this;
        }

        @Override
        public CommandCycle onSuccess() 
        {
            return HARVEST_RESOURCES;
        }
    },
    DO_TEST_MENUS()
    {
        @Override
        public String currentCommand()
        {
            return name();
        }

        @Override
        public CommandCycle onFail()
        {
            return DO_TEST_MENUS;
        }

        @Override
        public CommandCycle onSuccess() 
        {
            return null;
        }
    },
    DO_TEST_INSTANCE()
    {
        @Override
        public String currentCommand()
        {
            return name();
        }

        @Override
        public CommandCycle onFail()
        {
            return DO_TEST_INSTANCE;
        }

        @Override
        public CommandCycle onSuccess() 
        {
            return null;
        }
    };
}
