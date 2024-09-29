import {Link} from "react-router-dom";

const UomCategoryTree = () => {


    return (
        <div className="text-3xl text-amber-700">
            UOM CATEGORY TREE<br/>
            <Link to="/inventory/uom-categories/new">New Uom Category</Link><br/>
            <Link to="/inventory/uom-categories/detail/jdkcdcjbc-65645-rvrj65-vvrjkkjb">Detail Uom Category</Link>
            <br/>
        </div>
    );
}

export default UomCategoryTree;