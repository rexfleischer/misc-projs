<?php

class ThreadedForumBuilder {

    /**
     *
     * @param array $pages
     * @param array $rights
     * @param <type> $rightReq
     * @param <type> $bypass
     * @return _FORUM 
     */
    public static final function MakeFolderHierarchyForUser(
            array &$pages,
            array &$rights,
            $rightReq,
            $bypass){
        $forum = new _FORUM();
        $count = sizeof($pages);
        for($i=0; $i<$count; $i++){
            if ($bypass){
                $forum->placeNode($pages[$i]);
            } else {
                if ($pages[$i]->getIsPrivate()){
                    if (isset($rights[$pages[$i]->getPageID()])){
                        if ($rights[$pages[$i]->getPageID()] >= $rightReq){
                            $forum->placeNode($pages[$i]);
                        }
                    }
                } else {
                    $forum->placeNode($pages[$i]);
                }
            }
        }
        $forum->placeLost();
        return $forum;
    }

}

?>
