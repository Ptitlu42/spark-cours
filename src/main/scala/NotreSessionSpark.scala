import java.io.FileNotFoundException

import org.apache.log4j.{LogManager, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql._


object NotreSessionSpark {

  // Initialisation de la SessionSpark et du SparkContext
  var ss: SparkSession = null
  var spConf: SparkConf = null

  // Gestion des erreurs
  private val trace_log: Logger = LogManager.getLogger("Logger_console")

  /**
   * Fonction qui initialise et instancie une session spark
   * @parm ENV: c'est une variable qui indique l'environnement surlequel notre application est deployée
   *      si Env = true => l'application est en local, sinon elle est deployée sur un cluster
   */

  def Session_Spark(Env: Boolean): SparkSession = {
    try {
      if (Env == true){
        //System.setProperty("hadoop.home.dir", "C:/Hadoop/bin")
        ss = SparkSession.builder()
            .master("local[*]")
            .config("spark.sql.crossJoin.enable", "true")
            .getOrCreate()
      } else {
        ss = SparkSession.builder()
            .appName("Mon application Spark")
            .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .config("spark.sql.crossJoin.enable", "true")
            .getOrCreate()


      }
    } catch {
      case ex: FileNotFoundException => trace_log.error("Nous n'avons pas pu trouver le winutils dans le chemin indiqué" + ex.printStackTrace())
      case ex: Exception => trace_log.error("Erreur dans l'initialisation de la sessionSpark" + ex.printStackTrace())
    }
    ss

  }


}