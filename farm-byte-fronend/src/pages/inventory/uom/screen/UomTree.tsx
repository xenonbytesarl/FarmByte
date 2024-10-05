import {useNavigate} from "react-router-dom";
import {useSelector} from "react-redux";
import {
    getLoading,
    getPageSize,
    getTotalElements,
    getTotalPages, searchUoms,
    selectUoms
} from "@/pages/inventory/uom/UomSlice.ts";
import {useEffect, useState} from "react";
import {
    DEFAULT_DIRECTION_VALUE,
    DEFAULT_PAGE_VALUE,
    DEFAULT_SIZE_VALUE,
    MAX_SIZE_VALUE
} from "@/constants/page.constant.ts";
import useDebounce from "@/hooks/useDebounce.tsx";
import {DEBOUNCE_TIMEOUT} from "@/constants/app.constant.ts";
import {Direction} from "@/constants/directionConstant.ts";
import {useTranslation} from "react-i18next";
import {ColumnDef} from "@tanstack/react-table";
import {UomModel} from "@/pages/inventory/uom/UomModel.ts";
import {RootState, store} from "@/Store.ts";
import DataTable from "@/components/DataTable.tsx";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {
    findUomCategories, findUomCategoryById,
    selectUomCategoryById
} from "@/pages/inventory/uom-category/UomCategorySlice.ts";

const UomTree = () => {

    const uoms: Array<UomModel> = useSelector(selectUoms);
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
        direction: DEFAULT_DIRECTION_VALUE,
        keyword: keyword
    });

    const {t} = useTranslation(['home']);

    const columns: ColumnDef<UomModel>[] = [
        {
            accessorKey: "name",
            header: () => (<div className="text-start">{t("uom_tree_name")}</div>),
        },
        {
            accessorKey: "uomType",
            header: () => (<div className="text-start">{t("uom_tree_uom_type")}</div>),
            cell: ({row}) => (<div className="text-start capitalize">{row.original.uomType.toLocaleLowerCase()}</div>),
        },
        {
            accessorKey: "ratio",
            header: () => (<div className="text-left">{t("uom_tree_ratio")}</div>),
        },
        {
            accessorKey: "uomCategoryId",
            header: () => (<div className="text-left">{t("uom_tree_uom_category_id")}</div>),
            cell: ({row}) => (<div className="text-start">{uomCategoryByName(row.original.uomCategoryId)}</div>),
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
        store.dispatch(findUomCategories({...searchParam, size: MAX_SIZE_VALUE}));
    }, [store.dispatch]);

    useEffect(() => {
        store.dispatch(searchUoms({
            page: page,
            size: size,
            attribute: "name",
            direction: Direction.ASC,
            keyword: keyword
        }));
    }, [debounceKeyword, page, size]);

    const uomCategoryByName = (uomCategoryId: string): string => {
        const uomCategory =  useSelector((state: RootState) => selectUomCategoryById(state, uomCategoryId));
        return uomCategory.name;
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

    const handleEdit = (row: UomModel) => {
        navigate(`/inventory/uoms/detail/${row.id}`);
    }

    const handleNew = () => {
        navigate('/inventory/uoms/new');
    }

    const handleClear = () => {
        setKeyword('');
    }

    return (
        <div className="text-3xl text-amber-700 min-h-full">
            <DataTable
                title={'uom_tree_title'}
                columns={columns} data={uoms}
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

export default UomTree;