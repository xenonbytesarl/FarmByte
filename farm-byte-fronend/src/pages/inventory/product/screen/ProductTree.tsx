import {useNavigate} from "react-router-dom";
import {ProductModel} from "@/pages/inventory/product/ProductModel.ts";
import {useSelector} from "react-redux";
import {
    getLoading,
    getPageSize,
    getTotalElements, getTotalPages, searchProducts,
    selectProducts
} from "@/pages/inventory/product/ProductSlice.ts";
import {useEffect, useState} from "react";
import {DEFAULT_DIRECTION_VALUE, DEFAULT_PAGE_VALUE, MAX_SIZE_VALUE} from "@/constants/page.constant.ts";
import useDebounce from "@/hooks/useDebounce.tsx";
import {DEBOUNCE_TIMEOUT} from "@/constants/app.constant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {Direction} from "@/constants/directionConstant.ts";
import {useTranslation} from "react-i18next";
import {ColumnDef} from "@tanstack/react-table";
import {RootState, store} from "@/Store.ts";
import DataTable from "@/components/DataTable.tsx";
import {findUomCategories, selectUomCategoryById} from "@/pages/inventory/uom-category/UomCategorySlice.ts";
import {
    findProductCategories,
    selectProductCategoryById
} from "@/pages/inventory/product-category/ProductCategorySlice.ts";
import {findUoms, selectUomById} from "@/pages/inventory/uom/UomSlice.ts";

const ProductTree = () => {

    const products: Array<ProductModel> = useSelector(selectProducts);
    const pageSize: number = useSelector(getPageSize);
    const totalElements = useSelector(getTotalElements);
    const totalPages = useSelector(getTotalPages);
    const isLoading = useSelector(getLoading);
    const navigate = useNavigate();

    const [page, setPage] = useState<number>(DEFAULT_PAGE_VALUE);
    const [size, setSize] = useState<number>(pageSize);
    const [keyword, setKeyword] = useState<string>('');
    const debounceKeyword = useDebounce(keyword, DEBOUNCE_TIMEOUT );

    const [searchParam, setSearchParam] = useState<SearchParamModel>({
        page: DEFAULT_PAGE_VALUE,
        size: pageSize,
        attribute: "name",
        direction: Direction.ASC,
        keyword: keyword
    });

    const {t} = useTranslation(['home']);

    const columns: ColumnDef<ProductModel>[] = [
        {
            accessorKey: "reference",
            header: () => (<div className="text-left">{t("product_tree_reference")}</div>),
        },
        {
            accessorKey: "name",
            header: () => (<div className="text-left">{t("product_tree_name")}</div>),
        },
        {
            accessorKey: "type",
            header: () => (<div className="text-left">{t("product_tree_type")}</div>),
            cell: ({row}) => (
                <div className="text-left capitalize">{row.original.type.toLocaleLowerCase()}</div>
            )
        },
        {
            accessorKey: "categoryId",
            header: () => (<div className="text-left">{t("product_tree_category_id")}</div>),
            cell: ({row}) => (
                <div className="text-left capitalize">{productCategoryByName(row.original.categoryId)}</div>
            )
        },
        {
            accessorKey: "stockUomId",
            header: () => (<div className="text-left">{t("product_tree_stock_uom_id")}</div>),
            cell: ({row}) => (
                <div className="text-left capitalize">{uomName(row.original.stockUomId)}</div>
            )
        },
        {
            accessorKey: "action",
            header: () => "",
            cell: ({row}) => (
                <div  className="flex flex-row justify-end items-center gap-4 text-end ">
                    <span  onClick={() => handleEdit(row.original)} className="material-symbols-outlined text-primary text-xl cursor-pointer">edit</span>
                    <span  onClick={() => handleEdit(row.original)} className="material-symbols-outlined text-red-500 text-xl cursor-pointer">delete</span>
                </div>
            )
        }
    ];

    useEffect(() => {
        store.dispatch(findProductCategories({...searchParam, size: MAX_SIZE_VALUE}));
    }, [store.dispatch]);

    useEffect(() => {
        store.dispatch(findUoms({...searchParam, size: MAX_SIZE_VALUE}));
    }, [store.dispatch]);

    useEffect(() => {
        store.dispatch(searchProducts({
            page: page,
            size: size,
            attribute: "name",
            direction: DEFAULT_DIRECTION_VALUE,
            keyword: keyword
        }));
    }, [debounceKeyword, page, size]);

    const productCategoryByName = (productCategoryId: string): string => {
        const productCategory =  useSelector((state: RootState) => selectProductCategoryById(state, productCategoryId));
        return productCategory? productCategory.name: '';
    }

    const uomName = (uomId: string): string => {
        if(uomId) {
            const uom =  useSelector((state: RootState) => selectUomById(state, uomId));
            return uom? uom.name : '';
        }
        return '';
    }

    const handlePageChange = (page: number) => {
        setSearchParam({page: page, size: size, attribute: "name", direction: DEFAULT_DIRECTION_VALUE, keyword: keyword});
        //TODO add page in pageInfo in backend and manage page in store
        setPage(page);
    }

    const handleSizeChange = (size: number) => {
        setSearchParam({page: DEFAULT_PAGE_VALUE, size: size, attribute: "name", direction: DEFAULT_DIRECTION_VALUE, keyword: keyword});
        //TODO add page in pageInfo in backend and manage page in store
        setSize(size);
        setPage(DEFAULT_PAGE_VALUE);
    }

    const handleFilterChange = (keyword: string) => {
        setKeyword(keyword);
        setPage(DEFAULT_PAGE_VALUE);
    }

    const handleEdit = (row: ProductModel) => {
        navigate(`/inventory/products/detail/${row.id}`);
    }

    const handleNew = () => {
        navigate('/inventory/products/new');
    }

    const handleClear = () => {
        setKeyword('');
    }

    return (
        <div className="text-3xl text-amber-700 min-h-full">
            <DataTable
                title={'product_tree_title'}
                columns={columns} data={products}
                totalElements={totalElements}
                page={page}
                size={size}
                totalPages={totalPages}
                isLoading={isLoading}
                keyword={keyword}
                handlePageChange={handlePageChange}
                handleNew={handleNew}
                handleFilterChange={handleFilterChange}
                handleSizeChange={handleSizeChange}
                handleClear={handleClear}
            />
        </div>
    );
}

export default ProductTree;