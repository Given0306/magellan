/**
 * Copyright 2015 Ram Sriharsha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package magellan.index

import magellan.{BoundingBox, Point, Shape}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, Stack}

class ZOrderCurveIndexer(
    boundingBox: BoundingBox)
  extends Indexer[ZOrderCurve] {

  private val BoundingBox(xmin, ymin, xmax, ymax) = boundingBox

  override def index(point: Point, precision: Int): ZOrderCurve = {
    var currentPrecision = 0
    var evenBit = true
    var xrange = Array(xmin, xmax)
    var yrange = Array(ymin, ymax)
    var bits = 0L

    def encode(v: Double, range: Array[Double]): Unit = {
      val mid = range(0) + (range(1) - range(0))/ 2.0
      if (v < mid) {
        // add off bit
        bits <<= 1
        range.update(1, mid)
      } else {
        // add on bit
        bits <<= 1
        bits = bits | 0x1
        range.update(0, mid)
      }
      currentPrecision += 1
    }

    while (currentPrecision < precision) {
      if (evenBit) {
        encode(point.getX(), xrange)
      } else {
        encode(point.getY(), yrange)
      }
      evenBit = !evenBit
    }
    bits <<= (64 - precision)
    new ZOrderCurve(BoundingBox(xrange(0), yrange(0), xrange(1), yrange(1)), precision, bits)
  }

  override def index(shape: Shape, precision: Int): Seq[ZOrderCurve] = {
    val stack = new mutable.Stack[ZOrderCurve]()
    stack.push(new ZOrderCurve(boundingBox, 0, 0L))
    while (!stack.isEmpty) {
      val candidate = stack.pop()
      // if candidate is inside shape, include and continue
      // if candidate intersects shape, shape = shape \ GeoHash of candidate. push children onto stack
      // otherwise continue


    }
    ???
  }

}



