package com.github.dnvriend

import com.amazonaws.services.kinesis.producer.{ KinesisProducer, UserRecordResult }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object KplProducerTest extends App {

  val producer = new KinesisProducer()
  val x: Future[UserRecordResult] = for {
    result <- producer.addUserRecord("", "", null).asScala
  } yield result
}
