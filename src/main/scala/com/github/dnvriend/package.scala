package com.github
import com.google.common.util.concurrent.{ FutureCallback, Futures, ListenableFuture }

import scala.concurrent.{ Future, Promise }

package object dnvriend {
  implicit class toFuture[T](val that: ListenableFuture[T]) extends AnyVal {
    def asScala: Future[T] = {
      val p = Promise[T]
      Futures.addCallback(that, new FutureCallback[T] {
        override def onFailure(t: Throwable): Unit = p.failure(t)
        override def onSuccess(result: T): Unit = p.success(result)
      })
      p.future
    }
  }
}
