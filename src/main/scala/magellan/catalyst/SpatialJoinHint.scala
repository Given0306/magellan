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

package magellan.catalyst

import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical._

/**
  * A Spatial Join Hint node.
  */
case class SpatialJoinHint(child: LogicalPlan, hints: Map[String, String])
  extends UnaryNode {

  override def output: Seq[Attribute] = child.output

  //spark升级成员变量修饰符变化导致的问题
  //参考https://github.com/apache/spark/commit/d28d5732ae205771f1f443b15b10e64dcffb5ff0
  override def doCanonicalize(): LogicalPlan = SpatialJoinHint(child, hints)

  //spark升级新增接口导致的问题
  //https://github.com/apache/spark/commit/0945baf90660a101ae0f86a39d4c91ca74ae5ee3
  override protected def withNewChildInternal(newChild: LogicalPlan): LogicalPlan =
    copy(child = newChild)
}
