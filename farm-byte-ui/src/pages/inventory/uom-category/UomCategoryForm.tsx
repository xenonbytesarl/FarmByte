import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Input} from "@/components/ui/input.tsx";
import {useTranslation} from "react-i18next";
import {useSelector} from "react-redux";
import {
    createUomCategory,
    findUomCategoryById,
    getLoading,
    selectUomCategoryById,
    updateUomCategory
} from "@/pages/inventory/uom-category/UomCategorySlice.ts";
import {RootState, store} from "@/Store.ts";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {UomCategoryModel} from "@/pages/inventory/uom-category/UomCategoryModel.ts";
import {useForm} from "react-hook-form";
import {FormModeType} from "@/shared/model/FormModeType.ts";
import {z as Zod} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {unwrapResult} from "@reduxjs/toolkit";
import {useToast} from "@/hooks/use-toast.ts";
import {Toaster} from "@/components/ui/toaster.tsx";
import {cn} from "@/lib/utils.ts";
import FormCrudButton from "@/components/FormCrudButton.tsx";
import {ToastType} from "@/shared/constant/globalConstant.ts";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage
} from "@/components/ui/form.tsx";
import {Checkbox} from "@/components/ui/checkbox.tsx";
import {changeNullToEmptyString} from "@/utils/changeNullToEmptyString.ts";


const UomCategoryForm = () => {

    const {t} = useTranslation(['home']);

    const {uomCategoryId} = useParams();

    const {toast} = useToast();

    // @ts-ignore
    const uomCategory: UomCategoryModel = useSelector((state:RootState) => selectUomCategoryById(state, uomCategoryId));
    const isLoading: boolean = useSelector(getLoading);

    const navigate = useNavigate();
    const [mode, setMode] = useState<FormModeType>(uomCategoryId? FormModeType.READ: FormModeType.CREATE);

    const UomCategorySchema = Zod.object({
        id: Zod.string().min(0),
        name: Zod.string().min(1, {message: t('uom_category_form_name_required_message')}),
        parentUomCategoryId: Zod.string().min(0),
        active: Zod.boolean()
    });

    const defaultValuesUomCategory: UomCategoryModel = {
      id: "",
      name: "",
      parentUomCategoryId: "",
      active: true
    };
    const form = useForm<Zod.infer<typeof UomCategorySchema>>({
        defaultValues: defaultValuesUomCategory,
        resolver: zodResolver(UomCategorySchema),
        mode: "onBlur"
    });

    useEffect(() => {
        if(!uomCategory && uomCategoryId) {
            store.dispatch(findUomCategoryById(uomCategoryId));
        }
    }, [store.dispatch, uomCategory]);

    useEffect(() => {
        if(uomCategory) {
            form.reset(changeNullToEmptyString(uomCategory));
        }
    }, [uomCategory, form.reset]);

    const showToast = (variant: ToastType, message: string) => {
        toast({
            className: cn(
                'top-0 right-0 flex fixed md:max-w-[420px] md:top-4 md:right-4'
            ),
            variant: variant,
            title: "FarmByte",
            description: t(message),
        });
    }


    const onSubmit = () => {
        const uomCategoryFormValue: UomCategoryModel = form.getValues();
        if (uomCategoryFormValue.id) {
            store.dispatch(updateUomCategory({uomCategoryId: uomCategoryFormValue.id, uomCategory: uomCategoryFormValue}))
                .then(unwrapResult)
                .then((response) => {
                    setMode(FormModeType.READ);
                    // @ts-expect-error
                    showToast("info", response.message);
                })
                .catch((error) => {
                    setMode(FormModeType.EDIT);
                   showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                })
        } else {
            store.dispatch(createUomCategory(uomCategoryFormValue))
                .then(unwrapResult)
                .then((response) => {
                    setMode(FormModeType.READ);
                    // @ts-expect-error
                    showToast("success", response.message);
                    // @ts-expect-error
                    navigate(`/inventory/uom-categories/details/${response.data.content.id}`);
                })
                .catch((error) => {
                    setMode(FormModeType.CREATE);
                    showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                });
        }
    }

    const onEdit = () => {
        setMode(FormModeType.EDIT);
    }

    const onCancel = () => {
        if(uomCategory) {
            form.reset(changeNullToEmptyString(uomCategory));
            setMode(FormModeType.READ);
        } else {
            form.reset();
        }
    }

    const onCreate = () => {
        navigate('/inventory/uom-categories/new');
        setMode(FormModeType.CREATE);
        form.reset(defaultValuesUomCategory);
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} noValidate>
                <Toaster/>
                <Card className="">
                    <CardHeader>
                        <CardTitle className="flex flex-row justify-start items-center text-primary gap-5">
                        <span onClick={() => navigate(`/inventory/uom-categories`)}
                              className="material-symbols-outlined text-3xl cursor-pointer">arrow_back</span>
                            <span
                                className="text-2xl">{t(uomCategoryId ? 'uom_category_form_edit_title' : 'uom_category_form_new_title')}</span>
                        </CardTitle>
                        <CardDescription>
                    <span className="flex flex-row w-full m-5">
                        <FormCrudButton
                            mode={mode}
                            isLoading={isLoading}
                            isValid={form.formState.isValid}
                            onEdit={onEdit}
                            onCancel={onCancel}
                            onCreate={onCreate}
                        />
                        <span className="flex flex-row justify-end items-center gap-3 w-6/12">
                        </span>
                    </span>
                        </CardDescription>
                    </CardHeader>
                    <CardContent>
                        <FormField
                            control={form.control}
                            name="id"
                            render={({field}) => (
                                <FormItem>
                                    <FormControl>
                                        <Input id="id" type="hidden" {...field} />
                                    </FormControl>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="parentUomCategoryId"
                            render={({field}) => (
                                <FormItem>
                                    <FormControl>
                                        <Input id="parentUomCategoryId" type="hidden" {...field} />
                                    </FormControl>
                                </FormItem>
                            )}
                        />
                        <div className="grid w-full items-center gap-4">
                            <div className="flex flex-col space-y-1.5">
                                <FormField
                                    control={form.control}
                                    name="name"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel>{t('uom_category_form_name_label')}</FormLabel>
                                            <FormControl>
                                                <Input id="name" type="text" {...field}
                                                       disabled={mode === FormModeType.READ || isLoading}/>
                                            </FormControl>
                                            <FormMessage className="text-xs text-destructive"/>
                                        </FormItem>
                                    )}
                                />
                            </div>
                            <div className="flex flex-col space-y-1.5 mt-5">
                                <FormField
                                    control={form.control}
                                    name="active"
                                    render={({field}) => (
                                        <FormItem className="flex flex-row items-center space-x-2 space-y-0">
                                            <FormControl>
                                                <Checkbox id="active" checked={field.value}
                                                          disabled={mode === FormModeType.READ || isLoading}
                                                          onCheckedChange={field.onChange}/>
                                            </FormControl>
                                            <FormLabel
                                                className="font-normal">{t('uom_category_form_active_label')}</FormLabel>
                                        </FormItem>
                                    )}
                                />
                            </div>
                        </div>
                    </CardContent>
                    <CardFooter className="flex justify-between py-5">
                    </CardFooter>
                </Card>
            </form>
        </Form>
    );
};

export default UomCategoryForm;