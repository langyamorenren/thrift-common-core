package huivo.core.common;

/**
 * Created by langya on 2014/12/9.
 */
public class ConfigConstants {

    public static String root = "/nomad_env";
    public static String server_ports = "/serverports";
    public static String core_db = "/coredb";
    public static String service_db = "/servicedb";
    public static String mongo = "/mongo";
    public static String stat_mongo = "/stat_mongo";
    public static String redis = "/redis";
    public static String core_server = "/coreserver";
    public static String service_server = "/serviceserver";
    public static String notifier_server = "/notifierserver";
    public static String xinge_config = "/xinge_config";

    public static final String HW_S3_DOMAIN = "/hw_s3/domain";
    public static final String HW_S3_ACCESS_KEY = "/hw_s3/access_key";
    public static final String HW_S3_SECRET_KEY = "/hw_s3/secret_key";


    public static final String AWS_S3_DOMAIN = "/aws_s3/domain";
    public static final String AWS_S3_ACCESS_KEY = "/aws_s3/access_key_id";
    public static final String AWS_S3_SECRET_KEY = "/aws_s3/secret_access_key";

    public static final String S3_SAVE_DIR = "/s3/save_dir";


    public static final String ZOOKEEPER_HOST = "/zookeeper/host";
    public static final String ZOOKEEPER_CONNECT_TIMEOUT = "/zookeeper/connect-timeout";
    public static final String ZOOKEEPER_SESSION_TIMEOUT = "/zookeeper/session-timeout";


    public static final String RABBIT_HOST = "/rabbit/host";
    public static final String RABBIT_PORT = "/rabbit/port";
    public static final String RABBIT_USERNAME = "/rabbit/username";
    public static final String RABBIT_PASSWORD = "/rabbit/password";
    public static final String RABBIT_VHOST = "/rabbit/vhost";

    public static String notifier_server_port = server_ports + "/notifier_server_port";
    public static String passport_server_port = server_ports + "/passport_server_port";
    public static String passport_server_websocket_port = server_ports + "/passport_server_websocket_port";
    public static String management_server_port = server_ports + "/management_server_port";
    public static String userimport_server_port = server_ports + "/userimport_server_port";
    public static String management_v4_server_port = server_ports + "/management_v4_server_port";
    public static String behavior_server_port = server_ports + "/behavior_server_port";
    public static String behaviortask_server_port = server_ports + "/behaviortask_server_port";
    public static String checkin_server_port = server_ports + "/checkin_server_port";
    public static String checkintask_server_port = server_ports + "/checkintask_server_port";
    public static String mongocommon_server_port = server_ports + "/mongocommon_server_port";
    public static String notice_server_port = server_ports + "/notice_server_port";
    public static String notice_v3_server_port = server_ports + "/notice_v3_server_port";
    public static String album_server_port = server_ports + "/album_server_port";
    public static String album_v3_server_port = server_ports + "/album_v3_server_port";
    public static String store_server_port = server_ports + "/store_server_port";
    public static String homework_server_port = server_ports + "/homework_server_port";
    public static String mongo_nodejs_server_port = server_ports + "/mongo_nodejs_server_port";
    public static String announce_server_port = server_ports + "/announce_server_port";
    public static String util_server_port = server_ports + "/util_server_port";
    public static String coin_server_port = server_ports + "/coin_server_port";
    public static String coin_v3_server_port = server_ports + "/coin_v3_server_port";
    public static String cointogift_server_port = server_ports + "/cointogift_server_port";
    public static String cointocash_server_port = server_ports + "/cointocash_server_port";
    public static String pushinfo_server_port = server_ports + "/pushinfo_server_port";
    public static String pushinfo_register_server_port = server_ports + "/pushinfo_register_server_port";
    public static String huivopay_server_port = server_ports + "/huivopay_server_port";
    public static String course_server_port = server_ports + "/course_server_port";
    public static String course_accomplish_server_port = server_ports + "/course_accomplish_server_port";
    public static String user_total_info_server_port = server_ports + "/user_total_info_server_port";
    public static String advertisement_server_port = server_ports + "/advertisement_server_port";
    public static String xuexue_server_port = server_ports + "/xuexue_server_port";
    public static String update_server_port = server_ports + "/update_server_port";
    public static String userpayconfig_server_port = server_ports + "/userpayconfig_server_port";
    public static String activity_server_port = server_ports + "/activity_server_port";

    public static String novice_pack_server_port = server_ports+"/novice_pack_server_port";

    public static String course_table_server_port = server_ports+"/course_table_server_port";
    //园长版
    public static String message_server_port = server_ports+"/message_server_port";
    public static String leader_server_port = server_ports+"/leader_server_port";
    public static String school_server_port = server_ports + "/school_server_port";
    public static String summary_server_port = server_ports + "/summary_server_port";
    public static String growth_archive_server_port = server_ports + "/growth_archive_server_port";
    public static String openpay_server_port = server_ports + "/openpay_server_port";

    public static String common_notice_server_port = server_ports + "/common_notice_server_port";

    //huivo-core  passport、management相关数据库
    public static String huivo_core_db_name = core_db + "/core_db_name";
    public static String huivo_core_db_port = core_db + "/core_db_port";
    public static String huivo_core_db_host = core_db + "/core_db_host";
    public static String huivo_core_db_user = core_db + "/core_db_user";
    public static String huivo_core_db_pwd = core_db + "/core_db_pwd";

    //huivo-service相关数据库
    public static String huivo_service_db_name = service_db + "/service_db_name";
    public static String huivo_service_db_port = service_db + "/service_db_port";
    public static String huivo_service_db_host = service_db + "/service_db_host";
    public static String huivo_service_db_user = service_db + "/service_db_user";
    public static String huivo_service_db_pwd = service_db + "/service_db_pwd";

    //mongo数据库
    public static String mongo_db_name = mongo + "/mongo_db_name";
    public static String mongo_db_hosts = mongo + "/mongo_db_hosts";
    public static String mongo_db_ports = mongo + "/mongo_db_ports";
    public static String mongo_db_poolsize = mongo + "/mongo_db_poolsize";
    public static String mongo_db_blocksize = mongo + "/mongo_db_blocksize";

    //mongo002数据库
    public static String stat_mongo_db_name = stat_mongo + "/stat_mongo_db_name";
    public static String stat_mongo_db_hosts = stat_mongo + "/stat_mongo_db_hosts";
    public static String stat_mongo_db_ports = stat_mongo + "/stat_mongo_db_ports";
    public static String stat_mongo_db_poolsize = stat_mongo + "/stat_mongo_db_poolsize";
    public static String stat_mongo_db_blocksize = stat_mongo + "/stat_mongo_db_blocksize";

    //redis数据库
    public static String redis_db_host = redis + "/redis_db_host";
    public static String redis_db_port = redis + "/redis_db_port";
    public static String core_server_host = core_server + "/core_server_host";
    public static String service_server_host = service_server + "/service_server_host";
    public static String notifier_server_host = notifier_server + "/notifier_server_host";

    //xinge config
    public static String parent_android_access_id = xinge_config + "/parent_android_access_id";
    public static String parent_android_access_key = xinge_config + "/parent_android_access_key";
    public static String parent_android_secret_key = xinge_config + "/parent_android_secret_key";
    public static String teacher_android_access_id = xinge_config + "/teacher_android_access_id";
    public static String teacher_android_access_key = xinge_config + "/teacher_android_access_key";
    public static String teacher_android_secret_key = xinge_config + "/teacher_android_secret_key";
    public static String parent_ios_access_id = xinge_config + "/parent_ios_access_id";
    public static String parent_ios_access_key = xinge_config + "/parent_ios_access_key";
    public static String parent_ios_secret_key = xinge_config + "/parent_ios_secret_key";
    public static String teacher_ios_access_id = xinge_config + "/teacher_ios_access_id";
    public static String teacher_ios_access_key = xinge_config + "/teacher_ios_access_key";
    public static String teacher_ios_secret_key = xinge_config + "/teacher_ios_secret_key";
}