<?php

require_once 'fcore/FCore.php';

FCore::init(
    array(

        'LOG_TO_DB'             => false,

        'LOG_TO_DB_LEVEL'       => 5,

        'LOG_TO_MAIL'           => false,

        'LOG_TO_MAIL_LEVEL'     => 5,

        'LOG_TO_HTML'           => true,

        'LOG_TO_HTML_LEVEL'     => 1,
        
        'LOG_TO_FILE'           => false,

        'LOG_TO_FILE_LEVEL'     => 1,

        'LOG_GLOBAL_LEVEL'      => 1,

        'CACHE'                 => false,

        'CACHE_DIR'             => BN_DIR_BASEPATH . "/_app/cache",

        'SESSION_CHECK_FINGERPRINT' => false,

        'SESSION_CHECK_HIJACK'  => true,

        'LOGIN_MAX_ATTEMPTS'    => 5,

        'LOGIN_TIME_FROZE'      => 600,

        'DIR_ROOT_DBFACTORIES'  => BN_DIR_BASEPATH . '/_app/dbfactories',
        
        'DIR_ROOT_MASTERS'      => BN_DIR_BASEPATH . '/_app/masters',

        'DIR_ROOT_COMMONOBJECTS'=> BN_DIR_BASEPATH . '/_app/common/objects',

        'DIR_ROOT_COMMONVIEWS'  => BN_DIR_BASEPATH . '/_app/common/views',

        'EMAIL_WEBMASTER'       => 'webmaster.rexfleischer@gmail.com',

        'EMAIL_WEBMASTER_ERROR' => 'Fatal Error From Boydsnest',

        'EMAIL_WEBMASTER_NOTICE' => 'Notices From Boydsnest',

        'EMAIL_WEBMASTER_FROM'  => 'boydsnest@invalid1.net',

        'DB_DEFAULT_USER'       => 'invalid1_bnentry',

        'DB_DEFAULT_PASS'       => 's0mePoo22',

        'DB_DEFAULT_DB'         => 'invalid1_boydsnest',

        'DB_DEFAULT_HOST'       => 'localhost'
    )
);

?>
