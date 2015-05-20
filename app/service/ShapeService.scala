package service

import java.sql.ResultSet
import play.api.db.DB
import scala.concurrent.Future

import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

case class BoundingBox(neLat: Double, neLng: Double, swLat: Double, swLng: Double)

trait ShapeService {

  def listAll(): Future[List[String]]

  def findByCoordinate(lat: Double, lng: Double): Future[Option[String]]
  def findByBoundingBox(boundingBox: BoundingBox): Future[List[String]]
}

class DemoShapeService extends ShapeService {

  class RsIterator(rs: ResultSet) extends Iterator[ResultSet] {
    def hasNext: Boolean = rs.next()
    def next(): ResultSet = rs
  }

  def listAll(): Future[List[String]] =
    Future {
      DB.withConnection(c => {
        val resultSet = c.prepareStatement("select district_name from shapes").executeQuery()
        val result = new RsIterator(resultSet).map(x => x.getString(1))
        result.toList
      })
    }

  def findByCoordinate(lat: Double, lng: Double): Future[Option[String]] = ???
  def findByBoundingBox(boundingBox: BoundingBox): Future[List[String]] = ???
}