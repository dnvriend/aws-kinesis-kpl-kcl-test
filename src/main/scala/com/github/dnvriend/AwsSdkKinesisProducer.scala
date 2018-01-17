package com.github.dnvriend

import java.nio.ByteBuffer

import com.amazonaws.services.kinesis.model.{ PutRecordsRequest, PutRecordsRequestEntry, PutRecordsResultEntry }
import com.amazonaws.services.kinesis.{ AmazonKinesis, AmazonKinesisClientBuilder }

import scala.collection.JavaConverters._

object KinesisRecord {
  def toRequestEntry(record: KinesisRecord): PutRecordsRequestEntry = {
    new PutRecordsRequestEntry()
      .withPartitionKey(record.partitionKey)
      .withData(ByteBuffer.wrap(record.data))
  }
}
case class KinesisRecord(partitionKey: String, data: Array[Byte])

object AwsSdkKinesisProducer {
  val kinesisSdk: AmazonKinesis = AmazonKinesisClientBuilder.defaultClient()

  /**
   * Writes multiple data records into a Kinesis stream in a single call (also referred to as a PutRecords request).
   * Use this operation to send data into the stream for data ingestion and processing.
   *
   * Please note that all error and retry strategy must be handled explicitly.
   */
  def produce(streamName: String, records: List[KinesisRecord]): List[PutRecordsResultEntry] = {
    kinesisSdk
      .putRecords(mapToRequest(streamName, mapRecordsToRequestEntry(records)))
      .getRecords
      .asScala
      .toList
  }

  def mapRecordsToRequestEntry(records: List[KinesisRecord]): List[PutRecordsRequestEntry] = {
    records
      .map(KinesisRecord.toRequestEntry)
  }

  def mapToRequest(streamName: String, entries: List[PutRecordsRequestEntry]): PutRecordsRequest = {
    new PutRecordsRequest()
      .withStreamName(streamName)
      .withRecords(entries: _*)
  }
}
