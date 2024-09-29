import {Link} from "react-router-dom";

const ProductTree = () => {
    return (
        <div className="text-3xl text-amber-700">
            PRODUCT TREE<br/>
            <Link to="/inventory/products/new">New Product </Link><br/>
            <Link to="/inventory/products/detail/jdkcdcjbc-65645-rvrj65-vvrjkkjb">Detail Product</Link>
            <br/>
        </div>
    );
}

export default ProductTree;