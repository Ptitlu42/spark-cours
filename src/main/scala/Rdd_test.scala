import NotreSessionSpark._
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.DataFrame

object Rdd_test {
    def main(args: Array[String]): Unit = {
        val MaSession: SparkSession = Session_Spark(true)
        val sc: SparkContext = MaSession.sparkContext
        sc.setLogLevel("OFF")
        val rdd_test: RDD[Int] = sc.parallelize(List(1, 2, 3, 4, 5))

        // val df = MaSession.read
        //     .format("com.databricks.spark.csv")
        //     .option("header", "true")
        //     .load("src/main/resources/election.csv")
            
        // df.show()
        
        // val total_votant = df.agg(sum("votants"))
        //     .first()
        //     .get(0)
            
        // println("La somme total des votants est de : " + total_votant)

        // val suffixes = df.columns
        //   .filter(_.startsWith("Nom"))
        //   .map(_.replaceAll("Nom", ""))
        //   .distinct
          
        // val candidatsDFs = suffixes.map(suffix => {
        //   val nomCol = s"Nom$suffix"
        //   val prenomCol = s"Prenom$suffix"
        //   val voixCol = s"Voix$suffix"
          
        //   df.select(
        //       col(nomCol).as("Nom"), 
        //       col(prenomCol).as("Prenom"), 
        //       col(voixCol).cast("double").as("Voix")
        //     )
        // })
        
        // val tousLesCandidats = candidatsDFs.reduce(_ union _)
        
        // val resultatFinal = tousLesCandidats
        //   .filter(col("Nom").isNotNull && col("Prenom").isNotNull)
        //   .groupBy("Nom", "Prenom")
        //   .agg(sum("Voix").as("Voix_Total"))
        //   .orderBy(desc("Voix_Total"))
        
        // println("Tous les candidats avec leur nombre total de voix:")
        // resultatFinal.show()

        // Lecture des fichiers src/main/resources/txt.txt et src/main/resources/xtx.txt
        // val txtFile = sc.textFile("src/main/resources/txt.txt")
        // val xtxFile = sc.textFile("src/main/resources/xtx.txt")
        
        // // Créer un RDD avec les fichiers
        // val motsTxt = txtFile.flatMap(ligne => ligne.split("\\s+"))
        // val motsXtx = xtxFile.flatMap(ligne => ligne.split("\\s+"))
        
        // // Compter le nombre de mots dans chaque fichier
        // val nombreMotsTxt = motsTxt.count()
        // val nombreMotsXtx = motsXtx.count()
        
        // println(s"Nombre de mots dans txt.txt: $nombreMotsTxt")
        // println(s"Nombre de mots dans xtx.txt: $nombreMotsXtx")
        
        // // Afficher chaque mot des fichiers
        // println("Mots dans txt.txt:")
        // motsTxt.collect().foreach(println)
        
        // println("\nMots dans xtx.txt:")
        // motsXtx.collect().foreach(println)

        val df_adresses = MaSession.read
            .format("com.databricks.spark.csv")
            .option("header", "false")
            .load("src/main/resources/full.csv")

        df_adresses.columns.foreach(println)


        val df_adresses_clean = df_adresses.select(
            col("id"),
            col("numero_voie"),
            col("code_postal"),
            col("nom_commune"),
            col("source"),
            col("latitude"),
            col("longitude")
        )

        df_adresses_clean.show(10)
    }
}
  