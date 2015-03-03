import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext._

/**
 * A simple Spark app in Scala
 */
object ScalaApp {

  def main(args: Array[String]) {

    val conf = new SparkConf()
      .setAppName("Test Spark App")
      .setMaster("local[4]")
      .set("spark.hadoop.validateOutputSpecs", "false")

    val sc = new SparkContext(conf)

    // we take the raw data in CSV format and convert it into a set of records of the form (user, product, price)
    val data = sc.textFile("data/UserPurchaseHistory.csv")
      .map(line => line.split(","))
      .map(purchaseRecord => (purchaseRecord(0), purchaseRecord(1), purchaseRecord(2)))

    // let's count the number of purchases
    val numPurchases = data.count()

    // let's count how many unique users made purchases
    val uniqueUsers = data.map { case (user, product, price) => user }.distinct().count()

    // let's sum up our total revenue
    val totalRevenue = data.map { case (user, product, price) => price.toDouble }.sum()

    // let's find our most popular product
    val productsByPopularity = data
      .map { case (user, product, price) => (product, 1) }
      .reduceByKey(_ + _)
      .collect()
      .sortBy(-_._2)
//
    println(productsByPopularity)

//    val mostPopular = productsByPopularity(0)

    // finally, print everything out
//    println("Total purchases: " + numPurchases)
//    println("Unique users: " + uniqueUsers)
//    println("Total revenue: " + totalRevenue)
//    println("Most popular product: %s with %d purchases".format(mostPopular._1, mostPopular._2))

//    println("Total revenue: " + productsByPopularity)

    sc.stop()
  }

}
