<?php

require_once '_public/_definitions.php';
require_once DIR_TEMPLATE_BASELAYOUT;
//require_once DIR_HELPER_UNDERCONSTRUCTION_FORCE;

class page_terms extends _baselayout {

    protected function thisPageLayout(){
        ?>

<div id="contactustext">
    Web Site Terms and Conditions of Use<br /><br />

    <u><b>1. Terms</b></u><br />

    <div class="indent1">By accessing this web site, you are agreeing to be bound by these web site Terms and
Conditions of Use, all applicable laws and regulations, and agree that you are
responsible for compliance with any applicable local laws. If you do not agree with
any of these terms, you are prohibited from using or accessing this site. The materials
contained in this web site are protected by applicable copyright and trade mark law.</div><br />

    <u><b>2. Use License</b></u><br />

   <div class="indent1">1. Permission is granted to temporarily download one copy of the materials
   (information or software) on R and L's web site for personal, non-commercial
   transitory viewing only. This is the grant of a license, not a transfer of
   title, and under this license you may not:</div>
         <div class="indent2">1. modify or copy the materials;<br />
         2. use the materials for any commercial purpose, or for any public display (commercial or non-commercial);<br />
         3. attempt to decompile or reverse engineer any software contained on R and L's web site;<br />
         4. remove any copyright or other proprietary notations from the materials; or<br />
         5. transfer the materials to another person or "mirror" the materials on any other server.<br /></div>

   <div class="indent1">2. This license shall automatically terminate if you violate any of these restrictions
   and may be terminated by R and L at any time. Upon terminating your viewing of
   these materials or upon the termination of this license, you must destroy any
   downloaded materials in your possession whether in electronic or printed format.<br /><br /></div>

    <u><b>3. Disclaimer</b></u><br />

   <div class="indent1">1. The materials on R and L's web site are provided "as is". R and L makes no
   warranties, expressed or implied, and hereby disclaims and negates all other
   warranties, including without limitation, implied warranties or conditions of
   merchantability, fitness for a particular purpose, or non-infringement of intellectual
   property or other violation of rights. Further, R and L does not warrant or make
   any representations concerning the accuracy, likely results, or reliability of
   the use of the materials on its Internet web site or otherwise relating to such
   materials or on any sites linked to this site.<br /><br /></div>

    <u><b>4. Limitations</b></u><br />

<div class="indent1">In no event shall R and L or its suppliers be liable for any damages (including,
without limitation, damages for loss of data or profit, or due to business interruption,)
arising out of the use or inability to use the materials on R and L's Internet site,
even if R and L or a R and L authorized representative has been notified orally or
in writing of the possibility of such damage. Because some jurisdictions do not allow
limitations on implied warranties, or limitations of liability for consequential or
incidental damages, these limitations may not apply to you.<br /><br /></div>

    <u><b>5. Revisions and Errata</b></u><br />

<div class="indent1">The materials appearing on R and L's web site could include technical, typographical,
or photographic errors. R and L does not warrant that any of the materials on its web
site are accurate, complete, or current. R and L may make changes to the materials
contained on its web site at any time without notice. R and L does not, however,
make any commitment to update the materials.<br /><br /></div>

    <u><b>6. Links</b></u><br />

<div class="indent1">R and L has not reviewed all of the sites linked to its Internet web site and is
not responsible for the contents of any such linked site. The inclusion of any
link does not imply endorsement by R and L of the site. Use of any such linked
web site is at the user's own risk.<br /><br /></div>

    <u><b>7. Site Terms of Use Modifications</b></u><br />

<div class="indent1">R and L may revise these terms of use for its web site at any time without notice.
By using this web site you are agreeing to be bound by the then current version of
these Terms and Conditions of Use.<br /><br /></div>

    <u><b>8. Governing Law</b></u><br />

<div class="indent1">Any claim relating to R and L's web site shall be governed by the laws of the State
of Seattle without regard to its conflict of law provisions.<br /><br /></div>

General Terms and Conditions applicable to Use of a Web Site.<br /><br />

    <u><b>Privacy Policy</b></u><br />

<div class="indent1">Your privacy is very important to us. Accordingly, we have developed this Policy
in order for you to understand how we collect, use, communicate and disclose and
make use of personal information. The following outlines our privacy policy.</div>

    <div class="indent2">* Before or at the time of collecting personal information, we will identify
    the purposes for which information is being collected.<br />
    * We will collect and use of personal information solely with the objective of
    fulfilling those purposes specified by us and for other compatible purposes,
    unless we obtain the consent of the individual concerned or as required by law.<br />
    * We will only retain personal information as long as necessary for the fulfillment of those purposes.<br />
    * We will collect personal information by lawful and fair means and, where appropriate, 
    with the knowledge or consent of the individual concerned.<br />
    * Personal data should be relevant to the purposes for which it is to be used, and, 
    to the extent necessary for those purposes, should be accurate, complete, and up-to-date.<br />
    * We will protect personal information by reasonable security safeguards against loss 
    or theft, as well as unauthorized access, disclosure, copying, use or modification.<br />
    * We will make readily available to customers information about our policies and practices 
    relating to the management of personal information.<br /><br /></div>

We are committed to conducting our business in accordance with these principles in order to
ensure that the confidentiality of personal information is protected and maintained.<br /><br />
</div>
        <?php
    }
    protected function thisPageStyle(){
        ?>
<style type="text/css">
<!--
#contactustext
{
    width: 960px;
    margin: 0px auto 0px auto;
}
.indent1
{
    width: 900px;
    margin: 0px auto 0px auto;
}
.indent2
{
    width: 800px;
    margin: 0px auto 0px auto;
}
-->
</style>
        <?php
    }
    protected function thisPagePreProcessing(){
    }
}

$thispage = new page_terms();
$thispage->EchoBaseLayout();

?>
