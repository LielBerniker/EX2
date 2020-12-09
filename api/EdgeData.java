package api;

public class EdgeData implements edge_data {
 private int Src;
 private int Dest;
 private double Edge_Weight;
 private String Edge_Info;
 private int Edge_Tag;

 public EdgeData(int src,int dest,double weight)
 {
 this.Src = src;
 this.Dest = dest;
 this.Edge_Weight = weight;
 this.Edge_Info = "";
 this.Edge_Tag = -1;
 }
 /**
  * The id of the source node of this edge.
  * @return
  */
 @Override
 public int getSrc() {
 return this.Src;
 }
 /**
  * The id of the destination node of this edge
  * @return
  */
 @Override
 public int getDest() {
 return this.Dest;
 }
 /**
  * @return the weight of this edge (positive value).
  */
 @Override
 public double getWeight() {
 return this.Edge_Weight;
 }
 /**
  * Returns the remark (meta data) associated with this edge.
  * @return
  */
 @Override
 public String getInfo() {
 return this.Edge_Info;
 }
 /**
  * Allows changing the remark (meta data) associated with this edge.
  * @param s
  */
 @Override
 public void setInfo(String s) {
 this.Edge_Info=s;
 }
 /**
  * Temporal data (aka color: e,g, white, gray, black)
  * which can be used be algorithms
  * @return
  */
 @Override
 public int getTag() {
 return this.Edge_Tag;
 }
 /**
  * This method allows setting the "tag" value for temporal marking an edge - common
  * practice for marking by algorithms.
  * @param t - the new value of the tag
  */
 @Override
 public void setTag(int t) {
 this.Edge_Tag=t;

 }
 }
