<?php

/**
 * Description of FCoreArray
 *
 * @author REx
 */
class FCoreArray {

    /**
     *
     * @param array $working the array that will have values swapped
     * @param <type> $field1 the first key to be swapped
     * @param <type> $field2 the second key to be swapped
     */
    public static final function swap(array &$arr, $field1, $field2){
        $temp = $arr[$field1];
        $arr[$field1] = $arr[$field2];
        $arr[$field2] = $temp;
    }
    /**
     *
     * @param array $arr
     * @param <type> $node
     * @param <type> $index
     */
    public static final function insert(array &$arr, $node, $index){
        $begin = array_slice($arr, 0, $index);
        $begin[] = $node;
        $end = array_slice($arr, $index);
        $arr = array_merge($begin, $end);
    }
    /**
     *
     * @param array $arr
     * @param <type> $index
     * @return <type>
     */
    public static final function remove(array &$arr, $index){
        if ($index == 0){ $arr = array_slice($arr, 1); return; }
        $begin = array_slice($arr, 0, $index-1);
        $end = array_slice($arr, $index);
        $arr = array_merge($begin, $end);
    }

    /**
     * 
     * @param array $old
     * @param array $new
     * @return array 
     */
    public static final function convolve(array &$old, array &$new){
        if ($old == null){ $old = array(); }
        if ($new == null){ $new = array(); }
        $r = array();
        foreach($old as $key => $val) { $r[$key] = $val; }
        foreach($new as $key => $val) { $r[$key] = $val; }
        return $r;
    }

}
?>
