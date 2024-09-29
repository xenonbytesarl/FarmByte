import {Link} from "react-router-dom";

const UomTree = () => {
    return (
        <div className="text-3xl text-amber-700">
            UOM TREE<br/>
            <Link to="/inventory/uoms/new">New Uom</Link><br/>
            <Link to="/inventory/uoms/detail/jdkcdcjbc-65645-rvrj65-vvrjkkjb">Detail Uom</Link>
            <br/>
        </div>
    );
}

export default UomTree;